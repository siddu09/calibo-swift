import json
import sys
import re

question = sys.argv[1]
answer = sys.argv[2]
context = sys.argv[3]
metric = sys.argv[4]

# --------------------------------------------------
# Normalize Text
# --------------------------------------------------

context_words = set(
    re.findall(r'\b\w+\b', context.lower())
)

answer_words = set(
    re.findall(r'\b\w+\b', answer.lower())
)

# --------------------------------------------------
# Ignore Noise Words
# --------------------------------------------------

IGNORE_WORDS = {
    "s1", "s2", "s3", "s4", "s5",
    "citation", "citations",
    "answer",

    # Common stop words
    "the", "a", "an",
    "is", "are", "was", "were",
    "of", "and", "in", "to",
    "for", "on", "at", "by",
    "with", "from", "as"
}

context_words = {
    word
    for word in context_words
    if word not in IGNORE_WORDS
}

answer_words = {
    word
    for word in answer_words
    if word not in IGNORE_WORDS
}

# --------------------------------------------------
# Coverage Calculation
# Measures how much of the answer
# is supported by context
# --------------------------------------------------

matched_words = context_words.intersection(
    answer_words
)

if len(answer_words) == 0:
    coverage_score = 0.0
else:
    coverage_score = (
        len(matched_words)
        / len(answer_words)
    )

# --------------------------------------------------
# Hallucination Penalty
# --------------------------------------------------

extra_words = answer_words - context_words

penalty_score = min(
    0.10,
    len(extra_words) * 0.005
)

# --------------------------------------------------
# Final Score
# --------------------------------------------------

final_score = max(
    0.0,
    min(
        1.0,
        coverage_score - penalty_score
    )
)

# --------------------------------------------------
# Threshold
# --------------------------------------------------

threshold = 0.30

if final_score >= 0.60:
    verdict = "STRONG_MATCH"

elif final_score >= 0.30:
    verdict = "LIKELY_MATCH"

else:
    verdict = "MISMATCH"

passed = verdict != "MISMATCH"

if verdict == "STRONG_MATCH":
    reason = "Strong answer-context alignment"

elif verdict == "LIKELY_MATCH":
    reason = "Partial answer-context alignment"

else:
    reason = "Answer not sufficiently supported by context"

# --------------------------------------------------
# Response
# --------------------------------------------------

result = {
    "score": round(final_score, 2),
    "coverageScore": round(coverage_score, 2),
    "penaltyScore": round(penalty_score, 2),
    "threshold": threshold,
    "passed": passed,
    "verdict": verdict,
    "reason": reason,
    "matchedWords": len(matched_words),
    "answerWords": len(answer_words)
}

print(json.dumps(result))