AUI.add("aui-portal-layout",function(X){var AP=X.Lang,w=AP.isBoolean,a=AP.isFunction,z=AP.isObject,P=AP.isString,J=AP.isValue,n=Math.ceil,H=X.DD.DDM,W="append",AM="circle",p="delegateConfig",o="down",g="drag",M="dragNode",N="dragNodes",c="dropContainer",V="dropNodes",AC="groups",s="icon",C="indicator",O="l",d="lazyStart",k="left",q="marginBottom",S="marginTop",AL="node",AA="offsetHeight",AB="offsetWidth",Y="placeholder",y="placeAfter",Q="placeBefore",AN="portal-layout",Z="prepend",AO="proxy",AE="proxyNode",I="r",m="region",AG="right",B=" ",r="target",AJ="triangle",e="up",T="placeholderAlign",t="quadrantEnter",U="quadrantExit",AI="quadrantOver",AK=0,x=0,b=0,G=0,f=function(A){return(A instanceof X.NodeList);},l=function(){return Array.prototype.slice.call(arguments).join(B);},D=function(A){return f(A)?A:X.all(A);},v=function(L,A){return parseInt(L.getStyle(A),10)||0;},K=X.ClassNameManager.getClassName,j=K(AN,g,C),i=K(AN,g,C,s),AD=K(AN,g,C,s,k),F=K(AN,g,C,s,AG),E=K(AN,g,r,C),AF=K(s),AQ=K(s,AM,AJ,O),AH=K(s,AM,AJ,I),h='<div class="'+j+'">'+'<div class="'+l(i,AD,AF,AH)+'"></div>'+'<div class="'+l(i,F,AF,AQ)+'"></div>'+"<div>";var u=X.Component.create({NAME:AN,ATTRS:{delegateConfig:{value:null,setter:function(R){var A=this;var L=X.merge({bubbleTargets:A,dragConfig:{},nodes:A.get(N),target:true},R);X.mix(L.dragConfig,{groups:A.get(AC),startCentered:true});return L;},validator:z},proxyNode:{setter:function(A){return P(A)?X.Node.create(A):A;}},dragNodes:{validator:P},dropContainer:{value:function(A){return A;},validator:a},dropNodes:{value:false,setter:D},groups:{value:[AN]},lazyStart:{value:false,validator:w},placeholder:{value:h,setter:function(L){var A=P(L)?X.Node.create(L):L;if(!A.inDoc()){X.getBody().append(A.hide());}AK=v(A,q);x=v(A,S);A.addClass(E);b=v(A,q);G=v(A,S);return A;}},proxy:{value:null,setter:function(R){var A=this;var L={moveOnEnd:false,positionProxy:false};if(A.get(AE)){L.borderStyle=null;}return X.merge(L,R||{});}}},EXTENDS:X.Base,prototype:{initializer:function(){var A=this;A.bindUI();},bindUI:function(){var A=this;A.publish(T,{defaultFn:A._defPlaceholderAlign,queuable:false,emitFacade:true,bubbles:true});A._bindDDEvents();A._bindDropZones();},addDropNode:function(R,L){var A=this;R=X.one(R);if(!H.getDrop(R)){A.addDropTarget(new X.DD.Drop(X.merge({bubbleTargets:A,node:R},L)));}},addDropTarget:function(L){var A=this;L.addToGroup(A.get(AC));},alignPlaceholder:function(R,L){var A=this;var AR=A.get(Y);if(!A.lazyEvents){AR.show();}A._syncPlaceholderSize();AR.setXY(A.getPlaceholderXY(R,L));},calculateDirections:function(R){var L=this;var AR=L.lastY;var AS=L.lastX;var A=R.lastXY[0];var AT=R.lastXY[1];if(A!=AS){L.XDirection=(A<AS)?k:AG;}if(AT!=AR){L.YDirection=(AT<AR)?e:o;}L.lastX=A;L.lastY=AT;},calculateQuadrant:function(AW,L){var AZ=this;var AT=1;var AY=L.get(AL).get(m);var AV=AW.mouseXY;var AS=AV[0];var AR=AV[1];var AX=AY.top;var R=AY.left;var A=AX+(AY.bottom-AX)/2;var AU=R+(AY.right-R)/2;if(AR<A){AT=(AS>AU)?1:2;}else{AT=(AS<AU)?3:4;}AZ.quadrant=AT;return AT;},getPlaceholderXY:function(AU,L){var AY=this;var AX=AY.get(Y);var AS=AK;var A=x;if(L){AS=b;A=G;}AX.toggleClass(E,L);var AR=n(AU.bottom);var R=n(AU.left);var AW=n(AU.top);var AV=R;var AT=(AY.quadrant<3)?(AW-(AX.get(AA)+AS)):(AR+A);return[AV,AT];},removeDropTarget:function(L){var A=this;L.removeFromGroup(A.get(AC));},_alignCondition:function(){var A=this;var AS=H.activeDrag;var R=A.activeDrop;if(AS&&R){var AR=AS.get(AL);var L=R.get(AL);return !AR.contains(L);}return true;},_bindDDEvents:function(){var A=this;var L=A.get(p);var R=A.get(AO);A.delegate=new X.DD.Delegate(L);A.delegate.dd.plug(X.Plugin.DDProxy,R);A.on("drag:end",X.bind(A._onDragEnd,A));A.on("drag:enter",X.bind(A._onDragEnter,A));A.on("drag:exit",X.bind(A._onDragExit,A));A.on("drag:over",X.bind(A._onDragOver,A));A.on("drag:start",X.bind(A._onDragStart,A));A.after("drag:start",X.bind(A._afterDragStart,A));A.on(t,A._syncPlaceholderUI);A.on(U,A._syncPlaceholderUI);},_bindDropZones:function(){var A=this;A.get(V).each(function(R,L){A.addDropNode(R);});},_defPlaceholderAlign:function(AS){var A=this;var R=A.activeDrop;var AT=A.get(Y);if(R&&AT){var AR=R.get("node");var L=!!AR.drop;A.lastAlignDrop=R;A.alignPlaceholder(R.get(AL).get(m),L);}},_evOutput:function(){var A=this;return{drag:H.activeDrag,drop:A.activeDrop,quadrant:A.quadrant,XDirection:A.XDirection,YDirection:A.YDirection};},_fireQuadrantEvents:function(){var A=this;var AR=A._evOutput();var R=A.lastQuadrant;var L=A.quadrant;if(L!=R){if(R){A.fire(U,X.merge({lastDrag:A.lastDrag,lastDrop:A.lastDrop,lastQuadrant:A.lastQuadrant,lastXDirection:A.lastXDirection,lastYDirection:A.lastYDirection},AR));}A.fire(t,AR);}A.fire(AI,AR);A.lastDrag=H.activeDrag;A.lastDrop=A.activeDrop;A.lastQuadrant=L;A.lastXDirection=A.XDirection;A.lastYDirection=A.YDirection;},_getAppendNode:function(){return H.activeDrag.get(AL);},_positionNode:function(AS){var A=this;var AR=A.lastAlignDrop||A.activeDrop;if(AR){var AU=A._getAppendNode();var L=AR.get(AL);var R=J(L.drop);var AT=(A.quadrant<3);if(A._alignCondition()){if(R){L[AT?Q:y](AU);}else{var AV=A.get(c).apply(A,[L]);AV[AT?Z:W](AU);}}}},_syncPlaceholderUI:function(L){var A=this;if(A._alignCondition()){A.fire(T,{drop:A.activeDrop,originalEvent:L});}},_syncPlaceholderSize:function(){var A=this;var L=A.activeDrop.get(AL);var R=A.get(Y);if(R){R.set(AB,L.get(AB));}},_syncProxyNodeUI:function(R){var A=this;var AR=H.activeDrag.get(M);var L=A.get(AE);if(L&&!L.compareTo(AR)){AR.append(L);A._syncProxyNodeSize();}},_syncProxyNodeSize:function(){var A=this;var R=H.activeDrag.get(AL);var L=A.get(AE);if(R&&L){L.set(AA,R.get(AA));L.set(AB,R.get(AB));}},_afterDragStart:function(L){var A=this;if(A.get(AO)){A._syncProxyNodeUI(L);}},_onDragEnd:function(R){var A=this;var AR=A.get(Y);var L=A.get(AE);if(!A.lazyEvents){A._positionNode(R);}if(L){L.remove();}if(AR){AR.hide();}A.lastQuadrant=null;A.lastXDirection=null;A.lastYDirection=null;},_onDragEnter:function(L){var A=this;A.activeDrop=H.activeDrop;if(A.lazyEvents&&A.lastActiveDrop){A.lazyEvents=false;
A._syncPlaceholderUI(L);}if(!A.lastActiveDrop){A.lastActiveDrop=H.activeDrop;}},_onDragExit:function(L){var A=this;A._syncPlaceholderUI(L);A.activeDrop=H.activeDrop;A.lastActiveDrop=H.activeDrop;},_onDragOver:function(R){var A=this;var L=R.drag;if(A.activeDrop==H.activeDrop){A.calculateDirections(L);A.calculateQuadrant(L,A.activeDrop);A._fireQuadrantEvents();}},_onDragStart:function(L){var A=this;if(A.get(d)){A.lazyEvents=true;}A.lastActiveDrop=null;A.activeDrop=H.activeDrop;}}});X.PortalLayout=u;},"1.0.1",{skinnable:true,requires:["aui-base","dd-drag","dd-delegate","dd-drop","dd-proxy"]});