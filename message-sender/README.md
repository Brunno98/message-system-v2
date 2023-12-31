![diagram](docs/message-sender.svg)

---

### Docker commands

- docker run -p 5432:5432 -e POSTGRES_DB=postgres -e POSTGRES_HOST_AUTH_METHOD=trust -d postgres:13.11
- docker build -t message-sender .
- docker run --network host -d message-sender