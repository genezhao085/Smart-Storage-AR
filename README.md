# Smart-Storage-AR

A simple Compose Desktop MVP that helps users:
- create storage nodes,
- add items to nodes,
- preview a mock AR overlay panel.

The current implementation is intentionally local-first and uses fake in-memory data so the app is runnable without cloud services.

## Run

```bash
gradle :app:run
```

## Notes

- This is an MVP and avoids advanced architecture on purpose.
- Cloud sync, persistence, and real AR anchoring are deferred with TODO markers in code.
