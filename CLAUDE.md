---
name: CLAUDE configuration
description: Repository‑level configuration for Claude Code skills
---

## Agent skills

### Issue tracker

GitHub issue tracker is used via the `gh` CLI. See `docs/agents/issue-tracker.md`.

### Triage labels

Default label vocabulary (`needs-triage`, `needs-info`, `ready-for-agent`, `ready-for-human`, `wontfix`). See `docs/agents/triage-labels.md`.

### Domain docs

Single‑context layout: a single `CONTEXT.md` at the repo root and `docs/adr/` for architectural decision records. See `docs/agents/domain.md`.
