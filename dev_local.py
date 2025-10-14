#!/usr/bin/env python3
import shutil
import signal
import subprocess
import sys


def check_command_exists(cmd):
    if not shutil.which(cmd):
        print(f"Required command '{cmd}' not found in PATH.")
        sys.exit(1)


def cleanup(signum=None, _=None):
    print("\nShutting down Docker Compose...")
    try:
        subprocess.run(["docker", "compose", "down"], check=True)
    except Exception as exc:
        print(f"Error during docker compose down: {exc}")
    sys.exit(0)


def start_docker_compose():
    print("Starting Docker Compose...")
    try:
        subprocess.run(["docker", "compose", "up", "-d"], check=True)
    except subprocess.CalledProcessError as exception:
        print(f"Failed to start Docker Compose: {exception}")
        sys.exit(1)


def start_quarkus_dev():
    print("Starting Quarkus Dev...")
    proc = subprocess.Popen(
        ["./gradlew", "quarkusDev"],
        cwd="app"
    )
    try:
        proc.wait()
    except KeyboardInterrupt:
        print("\nInterrupted by user.")
    finally:
        if proc.poll() is None:
            proc.terminate()
            proc.wait()
        cleanup()


def main():
    check_command_exists("docker")
    signal.signal(signal.SIGINT, cleanup)
    signal.signal(signal.SIGTERM, cleanup)
    start_docker_compose()
    start_quarkus_dev()


if __name__ == "__main__":
    main()
