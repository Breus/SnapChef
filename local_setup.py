import subprocess
import sys
import key_setup


def ensure_executed_from_repo_root():
    if not key_setup.ensure_repo_root():
        print("Please run this script from the repository root directory.")
        sys.exit(1)


def install_precommit_hooks() -> None:
    subprocess.run(["Pre-commit", "install"], check=True)
    print("Pre-commit hook (re)installed.")


def main() -> None:
    ensure_executed_from_repo_root()
    install_precommit_hooks()
    key_setup.generate_rsa_key_pair_for_local_dev()
    print("Local development configuration completed!")


if __name__ == "__main__":
    main()
