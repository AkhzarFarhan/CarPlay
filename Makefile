.PHONY: push

# To use a custom commit message, run: make push m="your message"
# Otherwise, it defaults to "Auto-commit"
m ?= Auto-commit

push:
	git add .
	-git commit -m "$(m)"
	git push
