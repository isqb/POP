%%% @author marcus <marcus@laptopen>
%%% @copyright (C) 2012, marcus
%%% @doc
%%%
%%% @end
%%% Created : 29 May 2012 by marcus <marcus@laptopen>

-module(human).
-export([inithuman/2,humanloop/2]).

%%--------------------------------------------------------------------
%% @doc The human loop handling communication with the GUI and the 
%%      main loop
%% @spec humanloop(pid(),pid()) -> none()
%% @end
%%--------------------------------------------------------------------
humanloop(MainPID, GUIPID) ->
    receive
	{newposition, {CoordinateX,CoordinateY}} ->
	    GUIPID ! {move,human,self(),self(),CoordinateX,CoordinateY},
	    humanloop(MainPID, GUIPID);
	{move,w} -> 
    	MainPID ! {walk, self(), ["w"]},
	    humanloop(MainPID, GUIPID);
	{move,a} -> 
    	MainPID ! {walk, self(), ["a"]},
	    humanloop(MainPID, GUIPID);
	{move,s} -> 
    	MainPID ! {walk, self(), ["s"]},
	    humanloop(MainPID, GUIPID);
	{move,d} -> 
    	MainPID ! {walk, self(), ["d"]},
	    humanloop(MainPID, GUIPID);
	freeze ->
	    receive
		unfreeze ->
		    io:format("I'm unfrozen! (Human)"),
		    MainPID ! {unfreeze, self()},
		    humanloop(MainPID,GUIPID);
		kill ->
		    MainPID ! {unregister, humandeath, self()},
		    io:format("I'm dead (Human)")
	    end;
	exit ->
	    io:format("Gunmanloop with pid ~p exited~n",[self()])
    end.

%%--------------------------------------------------------------------
%% @doc Initiates a human controlled player and registers it to the 
%%      main process and enters humanloop
%% @spec inithuman(pid(),pid()) -> none()
%% @end
%%--------------------------------------------------------------------
inithuman(MainPID, GUIPID) ->
    io:format("Initiating...~n~n"),
    random:seed(now()),
    {CoordinateX,CoordinateY} = {random:uniform(99),random:uniform(99)},
    MainPID ! {register, self(), {CoordinateX,CoordinateY}},
    GUIPID ! {move,human,self(),self(),CoordinateX,CoordinateY},
    humanloop(MainPID, GUIPID).
