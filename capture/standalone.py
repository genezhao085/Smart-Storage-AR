"""CLI entry point for the standalone capture module."""

from __future__ import annotations

import argparse
import sys

from .module import CaptureError, CaptureModule


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description="Standalone capture module")
    parser.add_argument(
        "--source",
        choices=("synthetic", "webcam"),
        default="synthetic",
        help="Capture source (default: synthetic).",
    )
    parser.add_argument(
        "--output",
        default="artifacts/captured_frame.ppm",
        help="Output file path (default: artifacts/captured_frame.ppm).",
    )
    parser.add_argument(
        "--device-index",
        type=int,
        default=0,
        help="Webcam device index when --source=webcam.",
    )
    parser.add_argument(
        "--timeout",
        type=float,
        default=5.0,
        help="Timeout in seconds for webcam capture.",
    )
    return parser


def main(argv: list[str] | None = None) -> int:
    args = build_parser().parse_args(argv)

    capturer = CaptureModule()
    try:
        result = capturer.capture(
            output_path=args.output,
            source=args.source,
            device_index=args.device_index,
            timeout_seconds=args.timeout,
        )
    except CaptureError as exc:
        print(f"Capture failed: {exc}", file=sys.stderr)
        return 1

    print(
        f"Capture successful: source={result.source} size={result.width}x{result.height} output={result.output_path}"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
