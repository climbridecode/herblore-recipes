---
name: Issue tracker configuration
description: How Claude Code skills interact with the issue tracker for this repository
---

## Issue tracker

This repository uses **GitHub Issues**. The `gh` CLI will be used for creating, reading, and updating issues.

- Create a new issue: `gh issue create --title "<title>" --body "<description>"`
- List open issues: `gh issue list --state open`
- Add labels: `gh issue edit <issue-number> --add-label "<label>"`

If you use a different issue tracker in the future, update this file accordingly.
