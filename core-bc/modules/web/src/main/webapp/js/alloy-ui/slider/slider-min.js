/*
Copyright (c) 2010, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.com/yui/license.html
version: 3.1.1
build: nightly
*/
YUI.add("slider-base",function(C){var B=C.Attribute.INVALID_VALUE;function A(){A.superclass.constructor.apply(this,arguments);}C.SliderBase=C.extend(A,C.Widget,{initializer:function(){this.axis=this.get("axis");this._key={dim:(this.axis==="y")?"height":"width",minEdge:(this.axis==="y")?"top":"left",maxEdge:(this.axis==="y")?"bottom":"right",xyIndex:(this.axis==="y")?1:0};this.publish("thumbMove",{defaultFn:this._defThumbMoveFn,queuable:true});},renderUI:function(){var D=this.get("contentBox");this.rail=this.renderRail();this._uiSetRailLength(this.get("length"));this.thumb=this.renderThumb();this.rail.appendChild(this.thumb);D.appendChild(this.rail);D.addClass(this.getClassName(this.axis));},renderRail:function(){var E=this.getClassName("rail","cap",this._key.minEdge),D=this.getClassName("rail","cap",this._key.maxEdge);return C.Node.create(C.substitute(this.RAIL_TEMPLATE,{railClass:this.getClassName("rail"),railMinCapClass:E,railMaxCapClass:D}));},_uiSetRailLength:function(D){this.rail.setStyle(this._key.dim,D);},renderThumb:function(){this._initThumbUrl();var D=this.get("thumbUrl");return C.Node.create(C.substitute(this.THUMB_TEMPLATE,{thumbClass:this.getClassName("thumb"),thumbShadowClass:this.getClassName("thumb","shadow"),thumbImageClass:this.getClassName("thumb","image"),thumbShadowUrl:D,thumbImageUrl:D}));},bindUI:function(){this._bindThumbDD();this._bindValueLogic();this.after("disabledChange",this._afterDisabledChange);this.after("lengthChange",this._afterLengthChange);},_bindThumbDD:function(){var D={constrain:this.rail};D["stick"+this.axis.toUpperCase()]=true;this._dd=new C.DD.Drag({node:this.thumb,bubble:false,on:{"drag:start":C.bind(this._onDragStart,this)},after:{"drag:drag":C.bind(this._afterDrag,this),"drag:end":C.bind(this._afterDragEnd,this)}});this._dd.plug(C.Plugin.DDConstrained,D);},_bindValueLogic:function(){},_uiMoveThumb:function(D){if(this.thumb){this.thumb.setStyle(this._key.minEdge,D+"px");this.fire("thumbMove",{offset:D});}},_onDragStart:function(D){this.fire("slideStart",{ddEvent:D});},_afterDrag:function(E){var F=E.info.xy[this._key.xyIndex],D=E.target.con._regionCache[this._key.minEdge];this.fire("thumbMove",{offset:(F-D),ddEvent:E});},_afterDragEnd:function(D){this.fire("slideEnd",{ddEvent:D});},_afterDisabledChange:function(D){this._dd.set("lock",D.newVal);},_afterLengthChange:function(D){if(this.get("rendered")){this._uiSetRailLength(D.newVal);this.syncUI();}},syncUI:function(){this._dd.con.resetCache();this._syncThumbPosition();},_syncThumbPosition:function(){},_setAxis:function(D){D=(D+"").toLowerCase();return(D==="x"||D==="y")?D:B;},_setLength:function(E){E=(E+"").toLowerCase();var F=parseFloat(E,10),D=E.replace(/[\d\.\-]/g,"")||this.DEF_UNIT;return F>0?(F+D):B;},_initThumbUrl:function(){var E=this.get("thumbUrl"),F=this.getSkinName()||"sam",D=C.config.base+"slider/assets/skins/"+F;if(!E){E=D+"/thumb-"+this.axis+".png";this.set("thumbUrl",E);}},BOUNDING_TEMPLATE:"<span></span>",CONTENT_TEMPLATE:"<span></span>",RAIL_TEMPLATE:'<span class="{railClass}">'+'<span class="{railMinCapClass}"></span>'+'<span class="{railMaxCapClass}"></span>'+"</span>",THUMB_TEMPLATE:'<span class="{thumbClass}" tabindex="-1">'+'<img src="{thumbShadowUrl}" '+'alt="Slider thumb shadow" '+'class="{thumbShadowClass}">'+'<img src="{thumbImageUrl}" '+'alt="Slider thumb" '+'class="{thumbImageClass}">'+"</span>"},{NAME:"sliderBase",ATTRS:{axis:{value:"x",writeOnce:true,setter:"_setAxis",lazyAdd:false},length:{value:"150px",setter:"_setLength"},thumbUrl:{value:null,validator:C.Lang.isString}}});},"3.1.1",{requires:["widget","substitute","dd-constrain"]});YUI.add("slider-value-range",function(F){var B="min",E="max",D="value",C=Math.round;function A(){this._initSliderValueRange();}F.SliderValueRange=F.mix(A,{prototype:{_factor:1,_initSliderValueRange:function(){},_bindValueLogic:function(){this.after({minChange:this._afterMinChange,maxChange:this._afterMaxChange,valueChange:this._afterValueChange});},_syncThumbPosition:function(){this._calculateFactor();this._setPosition(this.get(D));},_calculateFactor:function(){var J=this.get("length"),H=this.thumb.getStyle(this._key.dim),I=this.get(B),G=this.get(E);J=parseFloat(J,10)||150;H=parseFloat(H,10)||15;this._factor=(G-I)/(J-H);},_defThumbMoveFn:function(I){var G=this.get(D),H=this._offsetToValue(I.offset);if(G!==H){this.set(D,H,{positioned:true});}},_offsetToValue:function(H){var G=C(H*this._factor)+this.get(B);return C(this._nearestValue(G));},_valueToOffset:function(G){var H=C((G-this.get(B))/this._factor);return H;},getValue:function(){return this.get(D);},setValue:function(G){return this.set(D,G);},_afterMinChange:function(G){this._verifyValue();this._syncThumbPosition();},_afterMaxChange:function(G){this._verifyValue();this._syncThumbPosition();},_verifyValue:function(){var H=this.get(D),G=this._nearestValue(H);if(H!==G){this.set(D,G);}},_afterValueChange:function(G){if(!G.positioned){this._setPosition(G.newVal);}},_setPosition:function(G){this._uiMoveThumb(this._valueToOffset(G));},_validateNewMin:function(G){return F.Lang.isNumber(G);},_validateNewMax:function(G){return F.Lang.isNumber(G);},_setNewValue:function(G){return C(this._nearestValue(G));},_nearestValue:function(J){var I=this.get(B),G=this.get(E),H;H=(G>I)?G:I;I=(G>I)?I:G;G=H;return(J<I)?I:(J>G)?G:J;}},ATTRS:{min:{value:0,validator:"_validateNewMin"},max:{value:100,validator:"_validateNewMax"},value:{value:0,setter:"_setNewValue"}}},true);},"3.1.1",{requires:["slider-base"]});YUI.add("clickable-rail",function(B){function A(){this._initClickableRail();}B.ClickableRail=B.mix(A,{prototype:{_initClickableRail:function(){this._evtGuid=this._evtGuid||(B.guid()+"|");this.publish("railMouseDown",{defaultFn:this._defRailMouseDownFn});this.after("render",this._bindClickableRail);this.on("destroy",this._unbindClickableRail);},_bindClickableRail:function(){this._dd.addHandle(this.rail);this.rail.on(this._evtGuid+"mousedown",this._onRailMouseDown,this);},_unbindClickableRail:function(){if(this.get("rendered")){var C=this.get("contentBox"),D=C.one("."+this.getClassName("rail"));
D.detach(this.evtGuid+"*");}},_onRailMouseDown:function(C){if(this.get("clickableRail")&&!this.get("disabled")){this.fire("railMouseDown",{ev:C});}},_defRailMouseDownFn:function(I){I=I.ev;var C=this._resolveThumb(I),F=this._key.xyIndex,G=parseFloat(this.get("length"),10),E,D,H;if(C){E=C.get("dragNode");D=parseFloat(E.getStyle(this._key.dim),10);H=this._getThumbDestination(I,E);H=H[F]-this.rail.getXY()[F];H=Math.min(Math.max(H,0),(G-D));this._uiMoveThumb(H);C._handleMouseDownEvent(I);}},_resolveThumb:function(D){var E=this._dd.get("primaryButtonOnly"),C=!E||D.button<=1;return(C)?this._dd:null;},_getThumbDestination:function(F,E){var D=E.get("offsetWidth"),C=E.get("offsetHeight");return[(F.pageX-Math.round((D/2))),(F.pageY-Math.round((C/2)))];}},ATTRS:{clickableRail:{value:true,validator:B.Lang.isBoolean}}},true);},"3.1.1",{requires:["slider-base"]});YUI.add("range-slider",function(A){A.Slider=A.Base.build("slider",A.SliderBase,[A.SliderValueRange,A.ClickableRail]);},"3.1.1",{requires:["slider-base","clickable-rail","slider-value-range"]});YUI.add("slider",function(A){},"3.1.1",{use:["slider-base","slider-value-range","clickable-rail","range-slider"]});