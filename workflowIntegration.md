## 🔗 Workflow Integration: Cross-Repository CI Trigger using `repository_dispatch` + GitHub PAT

This setup allows you to trigger **Repo B's** GitHub Actions workflow from **Repo A** when a pull request is created or updated. It uses `repository_dispatch` with a GitHub Personal Access Token (PAT).

---

### 🧭 Overview

- **Repo A**: Main dev repository (e.g., app code)
- **Repo B**: Contains BDD-Cucumber test framework
- When a PR is opened/updated in Repo A, Repo B is notified to run tests
- After tests run, results can be posted back to the original PR

---

### ✅ Setup Steps

#### 🔐 1. Generate a GitHub PAT from Repo 

The GitHub PAT (Personal Access Token) must be created by:

    Someone who has write access (or higher) to both repositories (Repo A and Repo B)
    — or at least the ability to trigger workflows and push to secrets in Repo A.

1. Go to [GitHub → Settings → Developer Settings → Personal Access Tokens](https://github.com/settings/tokens)
2. Generate a **classic token** with the following scopes:
    - `repo`
    - `workflow`
3. Copy the token

---

#### 🔒 2. Add the PAT to Repo A Secrets

- Go to **Repo A → Settings → Secrets → Actions**
- Add a new secret:
    - Name: `REPO_B_PAT`
    - Value: (paste your token)

---

#### 🧪 3. In Repo A: Trigger Repo B's Workflow

Create a workflow in `.github/workflows/trigger-tests.yml` in **Repo A**:

```yaml
name: Trigger Repo B BDD Tests

on:
  pull_request:
    branches:
      - main  # or dev

jobs:
  trigger:
    runs-on: ubuntu-latest
    steps:
      - name: Dispatch event to Repo B
        run: |
          curl -X POST https://api.github.com/repos/<your-org-or-user>/B/dispatches \
            -H "Authorization: token ${{ secrets.REPO_B_PAT }}" \
            -H "Accept: application/vnd.github+json" \
            -d '{
                  "event_type": "run-bdd-tests",
                  "client_payload": {
                    "pr_number": ${{ github.event.pull_request.number }},
                    "repo": "${{ github.repository }}",
                    "owner": "${{ github.repository_owner }}"
                  }
                }'
```
### Create .github/workflows/run-bdd.yml in Repo B:

```yaml
name: Run BDD Tests

on:
repository_dispatch:
types: [run-bdd-tests]

jobs:
bdd-tests:
runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v3

      - name: Set up Java
        uses: actions/setup-java@v3
        with:
          distribution: 'corretto'
          java-version: '17'

      - name: Run Tests
        run: mvn clean test -Dcucumber.filter.tags="@smoke"

      - name: Post Comment to Repo A PR
        run: |
          TEST_STATUS=" All BDD tests passed"
          if [ $? -ne 0 ]; then
            TEST_STATUS=" Some BDD tests failed"
          fi

          COMMENT="###  BDD Test Results from Repo B\n\n$TEST_STATUS"

          curl -X POST \
            -H "Authorization: token ${{ secrets.REPO_B_PAT }}" \
            -H "Accept: application/vnd.github+json" \
            https://api.github.com/repos/${{ github.event.client_payload.repo }}/issues/${{ github.event.client_payload.pr_number }}/comments \
            -d "$(jq -nc --arg body "$COMMENT" '{ body: $body }')"
```

### Add the PAT to Repo B
- Go to Repo B → Settings → Secrets → Actions

- Add:
    - Name: REPO_B_PAT
    - Value: same GitHub PAT used in Repo A

### 🧪 Example Result
- When a PR is opened in Repo A, the GitHub Actions workflow in Repo B will:
    - Run the test suite
    - Post a comment like this on the PR in Repo A:
  

### 🧪 BDD Test Results from Repo B
```css
✅ All BDD tests passed
```

#### or

### 🧪 BDD Test Results from Repo B
```css
❌ Some BDD tests failed
```
