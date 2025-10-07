import subprocess
import sys
from pathlib import Path


def generate_rsa_key_pair_for_local_dev() -> None:
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


def ensure_repo_root() -> bool:
    """Check if the script is being run from the repository root directory."""
    return Path(".git").is_dir() and Path("app").is_dir()


if __name__ == "__main__":
    if not ensure_repo_root():
        print("Please run this script from the repository root directory.")
        sys.exit(1)
    generate_rsa_key_pair_for_local_dev()
    print("RSA key pair setup completed!")
