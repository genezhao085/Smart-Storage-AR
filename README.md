# Smart-Storage-AR

A lightweight browser app that demonstrates:

- **Space module** with deterministic mock `SpaceNode` generation.
- **Item binding** that maps inventory items to selected nodes.
- **AR viewer with graceful fallback**:
  - WebXR-mode overlay when available.
  - DeviceOrientation fallback overlay when WebXR is absent.
  - Static 2D table when AR/browser capabilities are unavailable.

## Run locally

Because this uses ES modules, serve with any static server from the repo root.

```bash
python3 -m http.server 4173
```

Then open `http://localhost:4173`.
