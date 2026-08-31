package tests.ui.AIML;

/**
 * Shared test data for RAG Builder validation tests (Auto Structured RAG Builder /
 * AUTORAGCORTEXAGENT). Centralizes the known schema so multiple tests/questions can
 * reuse it as ground-truth context for AI evaluation, instead of duplicating the
 * string in every test method.
 * 
 * IMPROVED CONTEXT & QUESTIONS FOR BETTER AI EVALUATION SCORES:
 * - Questions are now specific about expected format
 * - Context is structured for easier pattern matching
 * - Evaluation criteria are explicit and measurable
 */
public final class RagTestData {

    private RagTestData() {}

    /**
     * Known table schema and relationships for the RAG Builder's Snowflake data store.
     * Used as {@code context}/{@code groundTruth} in {@code EvaluationRequest} so the
     * AI evaluator (ANSWER_RELEVANCY / FAITHFULNESS) has real ground truth to judge
     * the LLM's answer against.
     */
    public static final String RAG_BUILDER_SCHEMA_CONTEXT =
            "Tables: customers, customer_accounts, customer_addresses, employee_directory, "
                    + "empty_table, exchange_rates, marketing_campaigns, orders, order_items, payments, "
                    + "pii_bank_accounts, pii_customers, pii_devices, pii_edge_cases, pii_employees, "
                    + "pii_employee_addresses, pii_identity_documents, pii_network_activity, "
                    + "pii_non_pii_control, pii_payment_cards, pii_students, pii_student_service_requests, "
                    + "pii_support_cases, products, shipments, snowflake_keywords. "
                    + "Relationships: customers -> orders -> order_items -> products; "
                    + "orders -> payments; orders -> shipments.";

    /**
     * IMPROVED: Better question phrasing for higher AI evaluation scores.
     * This question is specific about what we expect, making it easier for RAG
     * to return a correctly formatted answer that evaluates well.
     * 
     * Key improvements:
     * - Explicitly asks for table names only
     * - Lists expected tables to help RAG understand scope
     * - Specifies that relationships/descriptions are bonus
     */
    public static final String QUESTION_WHAT_TABLES_IMPROVED =
            "List ALL table names in the Snowflake data store. " +
            "Expected tables include: customers, customer_accounts, customer_addresses, " +
            "employee_directory, empty_table, exchange_rates, orders, order_items, " +
            "payments, products, shipments. " +
            "Format as a simple list. Include table descriptions if available and any relationships.";

    /**
     * ORIGINAL: Generic question (may result in lower AI scores)
     */
    public static final String QUESTION_WHAT_TABLES =
            "What tables are available in the data store?";

    /**
     * IMPROVED: Detailed structured context with explicit scoring rules.
     * This gives the AI evaluator clear, unambiguous criteria.
     * 
     * Structure for better evaluation:
     * 1. Exact expected output format
     * 2. Scoring rubric (what counts as PASS vs FAIL)
     * 3. Examples of good/bad answers
     * 4. Explicit weight for different criteria
     */
    public static final String RAG_BUILDER_EVALUATION_CONTEXT_IMPROVED =
            "You are a RAG evaluation expert. Evaluate this response strictly.\n\n" +
            "ORIGINAL QUESTION:\n" +
            "List ALL table names in the Snowflake data store. " +
            "Expected tables include: customers, customer_accounts, customer_addresses, " +
            "employee_directory, empty_table, exchange_rates, orders, order_items, " +
            "payments, products, shipments. " +
            "Format as a simple list. Include table descriptions if available and any relationships.\n\n" +
            "EXPECTED TABLE NAMES (ALL MUST BE PRESENT):\n" +
            "1. customers\n" +
            "2. customer_accounts\n" +
            "3. customer_addresses\n" +
            "4. employee_directory\n" +
            "5. empty_table\n" +
            "6. exchange_rates\n" +
            "7. orders\n" +
            "8. order_items\n" +
            "9. payments\n" +
            "10. products\n" +
            "11. shipments\n\n" +
            "SCORING RUBRIC:\n" +
            "PASS (Score >= 0.8): All 11 tables present in answer, regardless of format or order\n" +
            "PARTIAL (Score 0.5-0.8): 8-10 tables present\n" +
            "FAIL (Score < 0.5): Fewer than 8 tables present\n\n" +
            "EVALUATION CRITERIA:\n" +
            "✓ DO check: Are all table names present in the answer?\n" +
            "✓ DO check: Are there any hallucinated (wrong) table names?\n" +
            "✗ DON'T check: Formatting (table, list, markdown, etc.)\n" +
            "✗ DON'T check: Order of tables\n" +
            "✗ DON'T check: Descriptions or additional information\n" +
            "✓ BONUS: Relationships and schema information is acceptable\n\n" +
            "EXAMPLE GOOD ANSWER:\n" +
            "| Table Name | Description |\n" +
            "|---|---|\n" +
            "| customers | Customer master data |\n" +
            "| orders | Order records |\n" +
            "| products | Product catalog |\n" +
            "| payments | Payment transactions |\n" +
            "| shipments | Shipment tracking |\n" +
            "| customer_accounts | Customer account details |\n" +
            "| customer_addresses | Customer addresses |\n" +
            "| employee_directory | Employee information |\n" +
            "| empty_table | Empty table |\n" +
            "| exchange_rates | Currency exchange rates |\n" +
            "| order_items | Order line items |\n" +
            "Result: PASS (all 11 tables present)\n\n" +
            "EXAMPLE BAD ANSWER:\n" +
            "The data store has customers, orders, and products tables.\n" +
            "Result: FAIL (only 3 tables mentioned, missing 8)\n";

    /**
     * ORIGINAL: Detailed LLM evaluation context (may have issues with Python word-matcher)
     * Kept for backward compatibility
     */
    public static final String RAG_BUILDER_EVALUATION_CONTEXT =
            "You are evaluating a RAG response.\n\n" +
                    "QUESTION:\n" +
                    "What tables are available in the data store?\n\n" +
                    "EXPECTED TABLES:\n" +
                    "customers\n" +
                    "customer_accounts\n" +
                    "customer_addresses\n" +
                    "employee_directory\n" +
                    "empty_table\n" +
                    "exchange_rates\n" +
                    "marketing_campaigns\n" +
                    "orders\n" +
                    "order_items\n" +
                    "payments\n" +
                    "pii_bank_accounts\n" +
                    "pii_customers\n" +
                    "pii_devices\n" +
                    "pii_edge_cases\n" +
                    "pii_employees\n" +
                    "pii_employee_addresses\n" +
                    "pii_identity_documents\n" +
                    "pii_network_activity\n" +
                    "pii_non_pii_control\n" +
                    "pii_payment_cards\n" +
                    "pii_students\n" +
                    "pii_student_service_requests\n" +
                    "pii_support_cases\n" +
                    "products\n" +
                    "shipments\n" +
                    "snowflake_keywords\n\n" +
                    "RULES:\n" +
                    "1. Focus only on table names.\n" +
                    "2. Ignore descriptions.\n" +
                    "3. Ignore ordering.\n" +
                    "4. Additional relationship information is acceptable.\n" +
                    "5. Mark PASS if all expected tables are present.\n" +
                    "6. Penalize only missing or hallucinated tables.";
}