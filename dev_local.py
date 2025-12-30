#!/usr/bin/env python3
import os
import shutil
import signal
import subprocess

import sys
import key_setup


def check_command_exists(cmd):
    if not shutil.which(cmd):
        print(f"Required command '{cmd}' not found in PATH.")
        sys.exit(1)


def cleanup(signum=None, _=None):
    print("\nShutting down Docker Compose...")
    try:
        subprocess.run(["docker", "compose", "-f", "docker-compose-local-dev.yml", "-f", "docker-compose.override.yml", "down"], check=True)
    except Exception as exc:
        print(f"Error during docker compose down: {exc}")
    sys.exit(0)


def verify_local_setup_completed():
    """Check if local_setup.py has been run at least once."""
    missing_components = []

    # Check for node_modules directory in ui folder (created by pnpm install)
    ui_node_modules = os.path.join("ui", "node_modules")
    if not os.path.exists(ui_node_modules):
        missing_components.append(f"UI dependencies (missing {ui_node_modules} directory)")

    # Check for RSA keys (created by key_setup.py)
    rsa_private_key = os.path.join("app", "src", "main", "resources", "local-dev-keys", "privateKey.pem")
    if not os.path.exists(rsa_private_key):
        missing_components.append(f"JWT keys (missing {rsa_private_key})")

    if missing_components:
        print("Error: Local setup has not been completed properly.")
        print("The following components were not found:")
        for component in missing_components:
            print(f"  - {component}")
        print("\nPlease run './local_setup.py' once to set up your development environment.")
        sys.exit(1)

    print("Local setup verification passed.")


def start_docker_compose():
    print("Starting Docker Compose...")
    try:
        subprocess.run(["docker", "compose", "-f", "docker-compose-local-dev.yml", "-f", "docker-compose.override.yml", "up", "-d"], check=True)
    except subprocess.CalledProcessError as exception:
        print(f"Failed to start Docker Compose: {exception}")
        sys.exit(1)


def setup_garage():
    print("Starting Garage...")
    try:
        bucket_name = "images"
        zone_name = "local"
        garage_command = ["docker", "exec", "-it", "snapchef-garage-1", "/garage"]
        output = subprocess.run(garage_command + ["status"], capture_output=True, text=True, check=True)
        if zone_name in output.stdout:
            print("Garage already set up.")
            return
        node_id = output.stdout.splitlines()[4].split(" ")[0]
        subprocess.run(garage_command + ["layout", "assign", node_id, "-z", zone_name, "-c", "1G"], check=True)
        subprocess.run(garage_command + ["layout", "apply", "--version", "1"], check=True)
        subprocess.run(garage_command + ["bucket", "create", bucket_name], check=True)
        output = subprocess.run(garage_command + ["key", "create", bucket_name + "-key"], capture_output=True, text=True, check=True)
        key_id = output.stdout.splitlines()[3].split(":")[1].strip()
        key_secret = output.stdout.splitlines()[5].split(":")[1].strip()
        with open("app/.env", "w") as file:
            file.write(f"S3_ACCESS_KEY_ID={key_id}\n")
            file.write(f"S3_ACCESS_KEY_SECRET={key_secret}\n")
        subprocess.run(garage_command + ["bucket", "allow", "--read", "--write", "--owner", bucket_name, "--key", bucket_name + "-key"], check=True)
    except subprocess.CalledProcessError as exception:
        print(f"Failed to setup Garage: {exception}")
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


def ensure_executed_from_repo_root():
    if not key_setup.ensure_repo_root():
        print("Please run this script from the repository root directory.")
        sys.exit(1)


def main():
    ensure_executed_from_repo_root()
    check_command_exists("docker")
    check_command_exists("pnpm")
    check_command_exists("pre-commit")
    verify_local_setup_completed()
    signal.signal(signal.SIGINT, cleanup)
    signal.signal(signal.SIGTERM, cleanup)
    start_docker_compose()
    setup_garage()
    start_quarkus_dev()


if __name__ == "__main__":
    main()
