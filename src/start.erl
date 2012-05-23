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
    spawn(fun()-> human:inithumanplayer(Main,GUIPID) end),
    MapDict = dict:new(),
    FrozenDict = dict:new(),
    mainloop:mainloop([], MapDict,GUIPID,FrozenDict).
