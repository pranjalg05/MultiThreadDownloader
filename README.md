#  MultiThreadDownloader

> A command-line application to download large files from a given URL using multiple threads for improved speed and efficiency.

---
## Features

-  Multithreaded downloading using Java `ExecutorService`
-  Supports custom number of threads
-  File size calculation
-  Range-based downloading with partial content
-  Merges downloaded chunks into a final file
-  Graceful error handling and timeout support

---

## How it Works

1. Takes a file URL and number of threads as input.
2. Fetches the content length using `HttpURLConnection`.
3. Splits the file into byte ranges (chunks).
4. Each thread downloads its chunk using HTTP range headers.
5. Chunks are written to temporary files.
6. All chunks are merged to produce the final file.
7. Temporary files are deleted.

---

## How to Run

### Requirements
- Java 8 or higher
- Internet connection
- File URL that supports partial content (`Accept-Ranges: bytes`)

### Run Command

```bash
javac *.java
java Main <file_url> <number_of_threads>

