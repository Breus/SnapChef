import subprocess
import sys
from pathlib import Path


def ensure_executed_from_repo_root():
    if not (Path(".git").is_dir() and Path("app").is_dir()):
        print("Please run this script from the repository root directory.")
        sys.exit(1)


def generate_rsa_key_pair_for_local_devl() -> None:
    resources_dir = Path("app/src/main/resources")
    private_key_path = resources_dir / "privateKey.pem"
    public_key_path = resources_dir / "META-INF/publicKey.pem"
    if private_key_path.exists() and public_key_path.exists():
        print("RSA key pair already exists. Skipping key generation.")
        return
    print("Generating RSA key pair in application resources...")
    public_key_path.parent.mkdir(parents=True, exist_ok=True)
    private_key_path.parent.mkdir(parents=True, exist_ok=True)

    subprocess.run([
        "openssl", "genpkey", "-algorithm", "RSA",
        "-out", str(private_key_path),
        "-pkeyopt", "rsa_keygen_bits:2048"
    ], check=True)

    subprocess.run([
        "openssl", "rsa", "-pubout",
        "-in", str(private_key_path),
        "-out", str(public_key_path)
    ], check=True)

    print(f"Private key generated at {private_key_path}")
    print(f"Public key extracted to {public_key_path}")


def install_precommit_hooks() -> None:
    subprocess.run(["pre-commit", "install"], check=True)
    print("Pre-commit hook (re)installed.")


def copy_env_example_to_env() -> None:
    env_example_path = Path(".env.example")
    env_path = Path(".env")
    if not env_path.exists():
        if env_example_path.exists():
            subprocess.run(["cp", str(env_example_path), str(env_path)], check=True)
            print("Copied .env.example to .env")
        else:
            print(".env.example does not exist. Please create a .env file manually.")
    else:
        print(".env file already exists. Skipping copy.")


def copy_pgpass_example_to_pgpass() -> None:
    pgpass_example_path = Path(".pgpass.example")
    pgpass_path = Path(".pgpass")
    if not pgpass_path.exists():
        if pgpass_example_path.exists():
            subprocess.run(["cp", str(pgpass_example_path), str(pgpass_path)], check=True)
            print("Copied .pgpass.example to .pgpass")
        else:
            print(".pgpass.example does not exist. Please create a .pgpass file manually.")
    else:
        print(".pgpass file already exists. Skipping copy.")

def main() -> None:
    ensure_executed_from_repo_root()
    install_precommit_hooks()
    generate_rsa_key_pair_for_local_devl()
    copy_env_example_to_env()
    copy_pgpass_example_to_pgpass()
    print("Local development configuration completed!")


if __name__ == "__main__":
    main()
