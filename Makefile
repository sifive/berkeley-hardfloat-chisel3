
default: build

BASE_DIR = $(abspath .)
BUILD_DIR ?= $(BASE_DIR)/build

CONFIG ?= F32ToRecF32

lookup_scala_srcs = $(shell find $(1)/ -iname "*.scala" 2> /dev/null)

IVY_DIR = $(BASE_DIR)/.ivy2
SBT = sbt -ivy $(IVY_DIR)

# FIRRTL
FIRRTL_REPO = https://github.com/freechipsproject/firrtl.git
FIRRTL_DIR = $(BASE_DIR)/firrtl
FIRRTL_COMMIT = 547082c04886f605b6f411b4ff3e42b54062f9a0
FIRRTL_STAMP = $(BASE_DIR)/firrtl.stamp

$(FIRRTL_DIR):
	git clone $(FIRRTL_REPO) $@ && \
    cd $(FIRRTL_DIR) && \
    git checkout $(FIRRTL_COMMIT) && \
    cd -

$(FIRRTL_STAMP): $(FIRRTL_DIR) $(call lookup_scala_srcs,$(FIRRTL_DIR))
	cd $(FIRRTL_DIR) && $(SBT) publishLocal
	touch $@

# Chisel 3
CHISEL3_REPO = https://github.com/freechipsproject/chisel3.git
CHISEL3_DIR = $(BASE_DIR)/chisel3
CHISEL3_COMMIT = e03e3f730250673ffa48c000caa369ed582467a2
CHISEL3_STAMP = $(BASE_DIR)/chisel3.stamp

$(CHISEL3_DIR):
	git clone $(CHISEL3_REPO) $@ && \
    cd $(CHISEL3_DIR) && \
    git checkout $(CHISEL3_COMMIT) && \
    cd -

$(CHISEL3_STAMP): $(FIRRTL_STAMP) $(CHISEL3_DIR) $(call lookup_scala_srcs,$(CHISEL3_DIR))
	cd $(CHISEL3_DIR) && $(SBT) publishLocal
	touch $@

setup: $(CHISEL3_STAMP)
	#git clone $(FIRRTL_REPO) $(FIRRTL_DIR)
	#git clone $(CHISEL3_REPO) $(CHISEL3_DIR)

build: $(CHISEL3_STAMP) $(call lookup_scala_srcs,src)
	$(SBT) "runMain hardfloat.Chisel3Main $(CONFIG) -td $(BUILD_DIR)"

clean:
	rm -rf $(BUILD_DIR)

.PHONY: clean setup build

