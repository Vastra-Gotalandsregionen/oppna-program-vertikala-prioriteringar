AUI.add("aui-delayed-task",function(B){var C=function(F,E,D){var A=this;A._args=D;A._delay=0;A._fn=F;A._id=null;A._scope=E||A;A._time=0;A._base=function(){var G=A._getTime();if(G-A._time>=A._delay){clearInterval(A._id);A._id=null;A._fn.apply(A._scope,A._args||[]);}};};C.prototype={delay:function(E,G,F,D){var A=this;if(A._id&&A._delay!=E){A.cancel();}A._delay=E||A._delay;A._time=A._getTime();A._fn=G||A._fn;A._scope=F||A._scope;A._args=D||A._args;if(!B.Lang.isArray(A._args)){A._args=[A._args];}if(!A._id){if(A._delay>0){A._id=setInterval(A._base,A._delay);}else{A._base();}}},cancel:function(){var A=this;if(A._id){clearInterval(A._id);A._id=null;}},_getTime:function(){var A=this;return(+new Date());}};B.DelayedTask=C;},"1.0.1",{skinnable:false});