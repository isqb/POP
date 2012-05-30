-module(start).
-export([start/0]).

start()->
    Main = self(),
    {ok,Host} = inet:gethostname(),
    HostFull = string:concat("sigui@",Host),
    HostAtom = list_to_atom(HostFull),
    GUIPID = {gui,HostAtom},
    GUIPID ! self(), %% send MainPID to GUI 
    spawn(fun()-> bot:initbot(Main,GUIPID) end),
    timer:sleep(1000),
    spawn(fun()-> bot:initbot(Main,GUIPID) end),
    timer:sleep(1000),
    spawn(fun()-> bot:initbot(Main,GUIPID) end),
    timer:sleep(1000),
    spawn(fun()-> human:inithuman(Main,GUIPID) end),
    MapDict = dict:new(),
    FrozenDict = dict:new(),
    mainloop:mainloop([], MapDict,GUIPID,FrozenDict).

restart(MainPID,GUIPID)->
    spawn(fun()-> bot:initbot(MainPID,GUIPID) end),
    timer:sleep(1000),
    spawn(fun()-> bot:initbot(MainPID,GUIPID) end),
    timer:sleep(1000),
    spawn(fun()-> bot:initbot(MainPID,GUIPID) end),
    timer:sleep(1000),
    spawn(fun()-> human:inithuman(MainPID,GUIPID) end),
    MapDict = dict:new(),
    FrozenDict = dict:new(),
    mainloop:mainloop([], MapDict,GUIPID,FrozenDict).
