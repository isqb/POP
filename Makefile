#################### 
GROUP_NUMBER := 01
####################

ERLC := erlc
ERLC_FLAGS := -W -I include

ERL_FILES := $(wildcard src/Game/*.erl)
BEAM_FILES := $(patsubst src/Game/%.erl,ebin/%.beam,${ERL_FILES})

comma:= ,
empty:=
space:= $(empty) $(empty)

EDOC_SRC := $(filter-out %_test.erl, $(ERL_FILES))
EDOC_SRC_LIST := [$(subst $(space),$(comma),$(patsubst src/Game/%.erl,'src/Game/%.erl', $(EDOC_SRC)))]

REQUIRED_DIR_NAME := pop_2012_project_group_$(GROUP_NUMBER)

PROJECT_DIR := $(notdir $(shell pwd))

USER=$(shell whoami)
ARCHIVE_NAME :=  $(REQUIRED_DIR_NAME)_archive_$(USER)_$(shell date "+%Y-%m-%d__%H:%M:%S")__.tar.gz
ARCHIVE_DIR := ..

all: $(BEAM_FILES)

ebin/%.beam: src/Game/%.erl
	$(ERLC) $(ERLC_FLAGS) -o ebin $<

start: all
	(java -jar "SuddenImpact.jar") &
	(cd ebin && erl -sname "player" -eval 'start:start()')

test: all
	(cd ebin && erl -noinput -eval 'eunit:test({dir, "."}, [verbose]), init:stop()')

doc: $(BEAM_FILES)
	erl -noshell -eval "edoc:files($(EDOC_SRC_LIST), [{dir, 'doc/html'}])" -s init stop

clean:
	rm -fr .#* *.dump
	rm -fr ebin/*.beam
	rm -fr src/Game/*.beam
	(cd doc/html && find . -name "*" -a ! -name overview.edoc -exec rm -rf {} \;)

remove_finderinfo:
	-xattr -d "com.apple.FinderInfo" src/Game/*.erl include/*.hrl doc/* doc/html/*

archive: clean
ifeq ($(REQUIRED_DIR_NAME), $(PROJECT_DIR))
	(cd $(ARCHIVE_DIR) && tar cvfz $(ARCHIVE_NAME) $(PROJECT_DIR) )
	@echo 
	@echo NOTE: Archive created in $(ARCHIVE_DIR)/$(ARCHIVE_NAME)
	@echo 
else
	@echo Error: Wrong directory name >$(PROJECT_DIR)<, change to >$(REQUIRED_DIR_NAME)<
endif

