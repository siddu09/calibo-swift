import json
import sys
import re

question = sys.argv[1]
answer = sys.argv[2]
context = sys.argv[3]
metric = sys.argv[4]

# --------------------------------------------------
# EXPECTED TABLES (from ground truth context)
# --------------------------------------------------

EXPECTED_TABLES = {
    "customers",
    "customer_accounts",
    "customer_addresses",
    "employee_directory",
    "empty_table",
    "exchange_rates",
    "orders",
    "order_items",
    "payments",
    "products",
    "shipments"
}

# --------------------------------------------------
# Extract Table Names from Answer
# Handles multiple formats: markdown table, plain list, etc.
# --------------------------------------------------

def extract_table_names(text):
    """Extract potential table names from answer"""
    found_tables = set()
    
    # Convert to lowercase for matching
    text_lower = text.lower()
    
    # Strategy 1: Look for exact table names
    for table_name in EXPECTED_TABLES:
        if table_name in text_lower:
            found_tables.add(table_name)
    
    # Strategy 2: Extract words from markdown table cells
    # Pattern: | word | (markdown table format)
    markdown_cells = re.findall(r'\|\s*([a-z_]+)\s*\|', text_lower)
    found_tables.update([cell for cell in markdown_cells if cell in EXPECTED_TABLES])
    
    # Strategy 3: Extract standalone table names (from list format)
    # Pattern: standalone word at line start/end
    words = re.findall(r'\b([a-z_]+)\b', text_lower)
    found_tables.update([word for word in words if word in EXPECTED_TABLES])
    
    return found_tables

# --------------------------------------------------
# Calculate Score Based on Table Coverage
# --------------------------------------------------

found_tables = extract_table_names(answer)
matched_count = len(found_tables)
expected_count = len(EXPECTED_TABLES)
missing_tables = EXPECTED_TABLES - found_tables

# Calculate coverage
if expected_count == 0:
    coverage_score = 0.0
else:
    coverage_score = matched_count / expected_count

# Calculate penalty for hallucinated tables (not in expected set)
# Extract all potential table-like words and check if they're wrong
answer_words = set(re.findall(r'\b([a-z_]+)\b', answer.lower()))
hallucinated = answer_words - EXPECTED_TABLES
# Only penalize if a significant number of extra table-like words
hallucination_penalty = min(0.15, len(hallucinated) * 0.002)

# Final score based on table coverage
final_score = max(0.0, min(1.0, coverage_score - hallucination_penalty))

# --------------------------------------------------
# Determine Verdict Based on Table Coverage
# --------------------------------------------------

if matched_count == expected_count:
    # All tables present
    verdict = "PASS"
    reason = f"All {expected_count} expected tables found"
    passed = True
    
elif matched_count >= expected_count * 0.9:
    # 90%+ tables present
    verdict = "STRONG_MATCH"
    reason = f"Found {matched_count}/{expected_count} expected tables"
    passed = True
    
elif matched_count >= expected_count * 0.7:
    # 70%+ tables present
    verdict = "LIKELY_MATCH"
    reason = f"Found {matched_count}/{expected_count} expected tables (missing: {', '.join(sorted(missing_tables)[:3])}...)"
    passed = True
    
else:
    # Less than 70% tables present
    verdict = "MISMATCH"
    reason = f"Only {matched_count}/{expected_count} tables found. Missing: {', '.join(sorted(missing_tables))}"
    passed = False

# --------------------------------------------------
# Response
# --------------------------------------------------

result = {
    "score": round(final_score, 2),
    "coverageScore": round(coverage_score, 2),
    "hallucintionPenalty": round(hallucination_penalty, 2),
    "threshold": 0.7,
    "passed": passed,
    "verdict": verdict,
    "reason": reason,
    "foundTables": matched_count,
    "expectedTables": expected_count,
    "missingTables": list(sorted(missing_tables))
}

print(json.dumps(result))
