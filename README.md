# Smart-Storage-AR

The app helps users digitize storage spaces, manage storage nodes, add items, and view a basic AR overlay.

## Capture module (standalone)

A standalone `capture` module is now available and can run independently of the rest of the app.

### Run synthetic capture (no dependencies)

```bash
python -m capture.standalone --source synthetic --output artifacts/captured_frame.ppm
```

### Run webcam capture (optional OpenCV)

```bash
python -m capture.standalone --source webcam --output artifacts/captured_frame.jpg
```

If OpenCV is not installed, webcam mode will exit with an informative error; synthetic mode still works.
