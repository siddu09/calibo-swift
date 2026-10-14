# AddProductToRelease: QA execution and UAT prerequisites

QA verification: 14 October 2026, Automation tenant, https://accelerate-qa.calibo.com.
Result: 1 test passed, 0 failures, no automatic retries. Product deletion passed.

## UI steps

1. Log in, select the Automation tenant, and complete Microsoft sign-in.
2. Open Release Trains from the navigation menu and create a new train with a unique Digital-prefixed name, the configured description, and tag Digital.
3. Open that train and add a release. Enter unique release name (Digital Platform Hotfix), version (2026.06), and release ID (DIGITAL-HOTFIX), plus the configured objective.
4. Select Release Manager(s) ai1.automation, Release Type Hotfix, Impact High, and Risk Medium.
5. Enter Sprint Name Digital Hotfix Acceptance Sprint, Timeline 01-Jun-2026 - 31-Dec-2026, Release Date 31-Dec-2026, and Release Status In Progress. Click Create and verify Release created successfully.
6. Open Products, click New Product, and choose Productize. Select the configured portfolio, enter a unique private product name and description, choose Product Engineering, and select Define, Design, Develop, and Deploy. Create the product.
7. Skip additional details, accept the unsaved-changes confirmation if shown, and choose No in the feature-creation prompt. Verify mandatory product details.
8. Open Releases, click Join Release, select the newly created release train and release, and click Select.
9. Fill Product Release Label Digital Platform Hotfix Acceptance, the configured Comments, and the configured Release Notes in its rich-text editor.
10. Select Product Release Manager ai1.automation, Release Status Completed, Stage UAT, and Customer(s) Customer_Automation. Stage UAT is a field value, not the execution environment.
11. Verify the fields and click Join. Verify Joined release successfully. and the joined release name.
12. Write the execution result, delete the created product with the configured reason, and verify Product deleted successfully.

## Fixes verified in QA

- Restored entry of Product Release Label, Comments, and Release Notes; it had been commented out while assertions remained enabled.
- Added two scoped XPath strategies for the Release Notes contenteditable editor. Validate editor text instead of an input value; the previous generic field locator selected a date input below Release Notes.

## UAT manager error

QA found ai1.automation in both Release Manager(s) and Product Release Manager. UAT data currently specifies the same QA user. This QA run does not establish whether that user exists or is eligible in UAT.

Check the exact selectable UAT display name and access under the intended tenant in both dropdowns. If ai1.automation is unavailable, either provision/enable the intended user with appropriate access or update these two values in src/test/resources/testdata/files/Pro/product_UAT.json under addProductToRelease.productRelease:

- manager
- productReleaseDropdowns.Product Release Manager

Also verify the configured tag Digital, customer Customer_Automation, portfolio, and business group exist in UAT. Do not silently select an unrelated manager to make the test pass.
