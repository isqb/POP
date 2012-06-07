-module(start).
-export([start/0]).

%%--------------------------------------------------------------------
%% @doc Starts the game engine, setting up communication and spawning
%%      three cpu-players and one human player then calls mainloop.
%% @spec start()->none()
%% @end
%%--------------------------------------------------------------------
start()->
    timer:sleep(1000),
    Main = self(),
    {ok,Host} = inet:gethostname(),
    HostFull = string:concat("sigui@",Host),
    HostAtom = list_to_atom(HostFull),
    GUIPID = {gui,HostAtom},
    GUIPID ! self(), %% send MainPID to GUI 
    spawn(fun()-> bot:initbot(Main,GUIPID) end),
    timer:sleep(10),
    spawn(fun()-> bot:initbot(Main,GUIPID) end),
    timer:sleep(10),
    spawn(fun()-> bot:initbot(Main,GUIPID) end),
    spawn(fun()-> human:inithuman(Main,GUIPID) end),
    MapDict = dict:new(),
    FrozenDict = dict:new(),
    mainloop:mainloop([], MapDict,GUIPID,FrozenDict).
