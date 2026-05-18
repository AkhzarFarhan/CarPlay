.PHONY: push

# Default commit message if 'm' is not provided
m ?= Minor changes

# Detect the Operating System
ifeq ($(OS),Windows_NT)
    # Windows Detection (CMD / PowerShell environment)
    # The || ver > nul ensures the Makefile continues if git commit returns an error code
    IGNORE_ERROR := || ver > nul
else
    # Linux / macOS Detection
    # Standard POSIX dash prefix ignores the command error code
    IGNORE_ERROR := 
endif

push:
	git add .
	$(if $(filter Windows_NT,$(OS)),git commit -m "$(m)" $(IGNORE_ERROR),-git commit -m "$(m)")
	git push
