
// Instantiating a Alloy "sandbox"
AUI().ready(
	'anim-base',
	'aui-base',
	'aui-tooltip',
	function(A) {

		var columnTriggerTooltip;
		var columnHideTrigger, columnShowTrigger, columnFilterNav, columnFilterNavInner, columnFilterSidebarContent;
		var columnShowAnim, columnHideAnim;
		var layout;
		var logoWrap;

		setupColumnToggle();
		
		// START - Column toggle
		function setupColumnToggle() {
			
			layout = A.one('#layout');
			columnFilterNav = A.one('#filterNav'); // detect via id
			columnFilterNavInner = A.one('.filter-sidebar');
			columnFilterSidebarContent = A.one('.filter-sidebar-content');
			columnHideTrigger = A.one('.column-control-hide');  // detect via class
			columnShowTrigger = A.one('.column-control-show');
			logoWrap = A.one('.logo-wrap');
			
			
			// If there is no columnFilterNaw - don't proceed
			if(!columnFilterNav) { return; }
			
            columnHideTrigger.on('click', onColumnHideClick);
            columnShowTrigger.on('click', onColumnShowClick);
            
//            columnHideTrigger.on('click', floatButtons);
//            columnShowTrigger.on('click', floatButtons);
			
			// Hide show trigger
			columnShowTrigger.hide();
			
			// Define animations to be used when showing or hiding columns
			
		    columnShowAnim = new A.Anim({
		    	node: columnFilterNav,
		    	to: { width: '250px' }
		    });		
			
		    columnHideAnim = new A.Anim({
		    	node: columnFilterNav,
		    	to: { width: '40px' }
		    });
		    
		    columnHideAnim.on('end', onColumnHideAnimEnd);
		    columnHideAnim.on('start', onColumnHideAnimStart);
		    columnShowAnim.on('end', onColumnShowAnimEnd);
		    columnShowAnim.on('start', onColumnShowAnimStart);

		    // Setup tooltips for column show/hide triggers
			columnTriggerTooltip = new A.Tooltip({
				trigger: '.column-control a',
				align: { points: [ 'tl', 'br' ] },
				width: '200px',
				title: true
			}).render();
		    
		    
		}
		// END - Column toggle
		

		// START - Handling of column hide event
		
		function onColumnHideClick(e) {
			
			// Stop default event
			e.halt();
			
			//columnFilterNav.setStyle('width', '100px');
			columnHideAnim.run();
			
			floatButtons();
		}
		
		// END - Handling of column hide event
		
		// START - Handling of column show event
		
		function onColumnShowClick(e) {
			
			e.halt();
			
			//columnFilterNav.setStyle('width', '300px');
			columnShowAnim.run();
			
			floatButtons();
		}
		
		// END - Handling of column showa event

		// START - Handling of column hide anim end event
		function onColumnHideAnimEnd(e) {
			
			// Toggle triggers
			columnShowTrigger.show();
			columnHideTrigger.hide();
			
			columnFilterSidebarContent.hide();
			layout.addClass('collapsed-filter-nav');
			
			floatButtons();
		}
		// END - Handling of column hide anim start event
		
		
		// START - Handling of column hide anim start event
		function onColumnHideAnimStart(e) {
			
			columnFilterNavInner.addClass('filter-sidebar-collapsed');
			
			// Hide logo
			logoWrap.hide();
			
			// Hide tooltip if shown
			columnTriggerTooltip.hide();
			
			floatButtons();
		}
		// END - Handling of column hide anim start event
		
		// START - Handling of column show anim end event
		function onColumnShowAnimEnd(e) {
			
			columnFilterNavInner.removeClass('filter-sidebar-collapsed');
			
			// Show logo
			logoWrap.show();
			
			// Toggle triggers
			columnShowTrigger.hide();
			columnHideTrigger.show();
			
			// Hide tooltip if shown
			columnTriggerTooltip.hide();
			YUI().use('node', 'gallery-timer', function (Y) {
		        var t = new Y.Timer({length:1000, repeatCount:2 , callback:floatButtons});
		        t.start();
			});
		}
		// END - Handling of column show anim end event
		
		// START - Handling of column show anim start event
		function onColumnShowAnimStart(e) {
			
			layout.removeClass('collapsed-filter-nav');
			
			columnFilterSidebarContent.show();
			
			floatButtons();
		}
		// END - Handling of column show anim start event
		
	}
);

function yuiCollectionToArray(yc) {
    var r = [];
    yc.each(function (node){
        r.push(node);
    });
    return r;
};

function alignDivsInTwoColumns(firstSelector, secondSelector) {
        
    function entwine(a,b) {
        return a.concat(b);
        var result = [];
        if (a.length > b.length) {
            var long = a, short = b;
        } else {
            var long = b, short = a;
        }
            
        for (var j = 0; j < long.length; j++) {
            result.push(long[j]);
            if (short.length < j && short[j]) result.push(short[j]);
        }
        
        var tmp = result;
        result = [];
        
        for (var j = 0; j < tmp.length; j++) {
            if (tmp[j] && tmp[j] != null) result.push(tmp[j]); 
        }
        
        return result;
    };
    
    function process() {
        try {
            for (var i = 0; i < 3; i++) {
                var date = new Date();
                var item1 = window.justifyCols.first.shift();
                var item2 = window.justifyCols.second.shift();
                if (!item1 || !item2) continue;
                var textsDivs = yuiCollectionToArray(item1.all('div'));
                if (textsDivs.length < 2) continue;
                var codesDivs = yuiCollectionToArray(item2.all('div'));
                var codeDiv = [];
                for (var j = 0; j < textsDivs.length; j++) {
                    if (!textsDivs[j] || !codesDivs[j]) return;
                    var t = textsDivs[j].getComputedStyle('height').replace('px','');
                    codeDiv.push('<div style="height:');
                    codeDiv.push(t);
                    codeDiv.push('px">');
                    codeDiv.push(codesDivs[j]._node.innerHTML);
                    codeDiv.push('</div>');
                }
                
                item2.setContent(codeDiv.join(''));
            }
        }catch(ee) {
            alert(ee.message);
        }
    };
    
    YUI().use('node', 'gallery-timer', function (Y) {
        var texts = yuiCollectionToArray(Y.all(firstSelector));
        var codes = yuiCollectionToArray(Y.all(secondSelector));
        
        if (!texts || !codes || texts.length != codes.length) {
            return;
        }

        var date = new Date();
        
        for (var i = 0; i < texts.length; i++) {
            if(yuiCollectionToArray(texts[i].all('div')).length < 3 ||
                    yuiCollectionToArray(codes[i].all('div')).length < 3) {
                texts.splice(i,1);
                codes.splice(i,1);
                i--;
            }
        }

        if (texts.length != codes.length || texts.length == 0) {
            return;
        }
        
        if (!window.justifyCols) window.justifyCols = {first: [], second: []};
        
        window.justifyCols.first = entwine(window.justifyCols.first, texts);
        window.justifyCols.second = entwine(window.justifyCols.second, codes);
        
        var t = new Y.Timer({length:300, repeatCount:(texts.length)/3 + 1, callback:process});
        t.start();
    });
}

try{
//    alignDivsInTwoColumns(
//            '.main-content td.diagnosTexts,.main-content td.aatgaerdskoderTexts,.main-content td.atcText', 
//            '.main-content td.diagnosKodTexts,.main-content td.aatgaerdskoder,.main-content td.atcKoder'
//    );
    
    alignDivsInTwoColumns(
            '.main-content td.diagnosTexts div.padded', 
            '.main-content td.diagnosKodTexts div.padded'
    );
    
    alignDivsInTwoColumns(
            '.main-content td.aatgaerdskoderTexts div.padded', 
            '.main-content td.aatgaerdskoder div.padded'
    );
    
    alignDivsInTwoColumns(
            '.main-content td.atcText div.padded', 
            '.main-content td.atcKoder div.padded'
    );
    
} catch(ee) {
    alert(ee.message);
}

//YUI().use('event', 'node', function(Y){
//    Y.on('scroll',function () {
//        var xy = Y.one('#pos').getXY();
//        if(!xy) return;
//        var buttonRow = Y.one('div.button-row');
//        var floatTable =  Y.one('#tmpFloatTitleTableId0TitleTable'); 
//        //buttonRow.setStyle('position', 'absolute');
//        buttonRow.setStyle('left', xy[0] + 'px');
//        xy = floatTable.getXY();
//        buttonRow.setStyle('top', xy[1] + 'px');
//        //buttonRow.setStyle('position', 'fixed');
//        //console.log(xy);
//        //console.log(buttonRow);
//    });
//});


    
    
    
