
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

        try {
        	setupColumnToggle();
        } catch (fail) {
            if (window['console']) console.log('setupColumnToggle failed.', fail);
            else alert(fail.message);
        }

		function initVariables(){
		    try {
            layout = A.one('#layout');
            columnFilterNav = A.one('#filterNav'); // detect via id
            columnFilterNavInner = A.one('.filter-sidebar');
            columnFilterSidebarContent = A.one('.filter-sidebar-content');
            columnHideTrigger = A.one('.column-control-hide');  // detect via class
            columnShowTrigger = A.one('.column-control-show');
            }catch (initVariablesException) {
                alert('failed in initVariables: ' + initVariablesException.message);
            }
		}

		// START - Column toggle
		function setupColumnToggle() {
			
            initVariables();

			// If there is no columnFilterNaw - don't proceed
			if(!columnFilterNav) { return; }
			
            columnHideTrigger.on('click', onColumnHideClick);
            columnShowTrigger.on('click', onColumnShowClick);
			
			// Hide show trigger
			//columnShowTrigger.hide();
			
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
		    try {
                columnTriggerTooltip = new A.Tooltip({
                    trigger: '.column-control a',
                    align: { points: [ 'tl', 'br' ] },
                    width: '200px',
                    title: true
                }).render();
		    } catch(e2) {
		        alert('on init columnTriggerTooltip: ' + e2.message);
		    }
		    
		}
		// END - Column toggle
		

		// START - Handling of column hide event
		
		function onColumnHideClick(e) {
			
			// Stop default event
			e.halt();

			columnHideAnim.run();
			
			runFloatButtons();
		}
		
		// END - Handling of column hide event
		
		// START - Handling of column show event
		
		function onColumnShowClick(e) {
			
			e.halt();

			columnShowAnim.run();
			
			runFloatButtons();
		}
		
		// END - Handling of column showa event

		// START - Handling of column hide anim end event
		function onColumnHideAnimEnd(e) {
            initVariables();
			// Toggle triggers
			columnShowTrigger.show();
			columnHideTrigger.hide();
			
			columnFilterSidebarContent.hide();
			layout.addClass('collapsed-filter-nav');
			
			runFloatButtons();
		}
		// END - Handling of column hide anim start event
		
		
		// START - Handling of column hide anim start event
		function onColumnHideAnimStart(e) {
			initVariables();
			columnFilterNavInner.addClass('filter-sidebar-collapsed');
			
			// Hide tooltip if shown
			columnTriggerTooltip.hide();
			
			runFloatButtons();
		}
		// END - Handling of column hide anim start event
		
		// START - Handling of column show anim end event
		function onColumnShowAnimEnd(e) {
			initVariables();
			columnFilterNavInner.removeClass('filter-sidebar-collapsed');
			
			// Toggle triggers
			columnShowTrigger.hide();
			columnHideTrigger.show();
			
			// Hide tooltip if shown
			columnTriggerTooltip.hide();
			runFloatButtons();
		}
		// END - Handling of column show anim end event
		
		// START - Handling of column show anim start event
		function onColumnShowAnimStart(e) {
			initVariables();
			layout.removeClass('collapsed-filter-nav');
			
			columnFilterSidebarContent.show();
			
			runFloatButtons();
		}
		// END - Handling of column show anim start event
		
	}
);

function runFloatButtons() {
    if(typeof((floatButtons)) !== 'undefined')
        YUI().use('node', 'gallery-timer', function (Y) {
            var t = new Y.Timer({length:1000, repeatCount:2 , callback:floatButtons});
            t.start();
        });
}

function initCodeAndTextAlignment() {

  function isElementInViewport (el) {
      if (isElementBeforeViewport(el) && isElementAfterViewport(el))
        return true;

      var rect = el.getBoundingClientRect();

      return rect.top >= 0 && rect.top <= (window.innerHeight || document.documentElement.clientHeight)
        || rect.bottom >= 0 && rect.bottom <= (window.innerHeight || document.documentElement.clientHeight);
  }

  function isElementBeforeViewport (el) {
      var rect = el.getBoundingClientRect();
      return rect.top <= 0;
  }


  function isElementAfterViewport (el) {
      var rect = el.getBoundingClientRect();
      return rect.bottom >= (window.innerHeight || document.documentElement.clientHeight);
  }

  function proecess(table) {
    var rows = table.rows;
    var c = 0;
    for (var i = 0; i < rows.length; i++) {
      var cells = rows[i].cells;
      if (isElementInViewport(rows[i])) {
        c++;
        var row = rows[i];
        if (!row['codeColumnsAligned']) {
          processRow(row);
          row['codeColumnsAligned'] = true;
        }
      }
    }
  }

  function processRow(row) {
    if (row == null || row == undefined) return;
    for(var i = 0; i < columns.length; i++) {
        processTd(columns[i], row);
    }
  }

  function processTd(meta, row) {
    if (meta.kodCol == -1 || meta.textCol == -1 || row.cells[0].tagName == 'TH'){
      return;
    }
    var textCell = row.cells[meta.textCol];
    if (textCell.children.length == 0) return;
    var textsDivs = textCell.children[0].children;
    if (textsDivs.length < 2) return;

    var codeCell = row.cells[meta.kodCol];

    var codesDivs = codeCell.children[0].children;

    var codeDiv = [];
    codeDiv.push('<div style="padding: 5px">');
    for (var j = 0; j < textsDivs.length; j++) {
        if (!textsDivs[j] || !codesDivs[j]) return;
        var t = textsDivs[j].clientHeight;
        if (t == 0) t = textsDivs[j].offsetHeight;
        codeDiv.push('<div style="height:');
        codeDiv.push(t);
        codeDiv.push('px;">');
        codeDiv.push(codesDivs[j].innerHTML);
        codeDiv.push('</div>');
    }
    codeDiv.push('</div>');
    codeCell.innerHTML = codeDiv.join('');
  }

  function indexOfCssClassTd(table, cssText) {
    var rows = table['rows'];
    if (!rows) return -1;
    if (rows.length < 3) return -1;
    var cells = rows[2].cells;
    for (var i = 0; i < cells.length; i++) {
      var cell = cells[i];
      if (cell['className'] == cssText) {
        return i;
      }
    }
    return -1;
  }

  var mainTable = document.getElementById('resultTable');

  function handler() {
    initColumns();
    proecess(mainTable);
  }

  var columns = [
    {text: 'diagnosTexts', kod: 'diagnosKodTexts'},
    {text: 'aatgaerdskoderTexts', kod: 'aatgaerdskoder'},
    {text: 'atcText', kod: 'atcKoder'}
  ];

function initColumns() {
  var mainTable = document.getElementById('resultTable');
  for(var i = 0; i < columns.length; i++) {
    var column = columns[i];
    column.textCol = indexOfCssClassTd(mainTable, column.text);
    column.kodCol = indexOfCssClassTd(mainTable, column.kod);
  }
}



  if (window.addEventListener) {
      addEventListener('DOMContentLoaded', handler, false);
      addEventListener('load', handler, false);
      addEventListener('scroll', handler, false);
      addEventListener('resize', handler, false);
  } else if (window.attachEvent)  {
      attachEvent('onDOMContentLoaded', handler); // IE9+ :(
      attachEvent('onload', handler);
      attachEvent('onscroll', handler);
      attachEvent('onresize', handler);
  }

}

if (window.addEventListener) {
  addEventListener('load', initCodeAndTextAlignment, false);
} else if (window.attachEvent)  {
  attachEvent('onload', initCodeAndTextAlignment);
}