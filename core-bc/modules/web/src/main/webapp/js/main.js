
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
			
			
		}
		
		// END - Handling of column hide event
		
		// START - Handling of column show event
		
		function onColumnShowClick(e) {
			
			e.halt();
			
			//columnFilterNav.setStyle('width', '300px');
			columnShowAnim.run();
			
		}
		
		// END - Handling of column showa event

		// START - Handling of column hide anim end event
		function onColumnHideAnimEnd(e) {
			
			// Toggle triggers
			columnShowTrigger.show();
			columnHideTrigger.hide();
			
			columnFilterSidebarContent.hide();
			layout.addClass('collapsed-filter-nav');
		}
		// END - Handling of column hide anim start event
		
		
		// START - Handling of column hide anim start event
		function onColumnHideAnimStart(e) {
			
			columnFilterNavInner.addClass('filter-sidebar-collapsed');
			
			// Hide logo
			logoWrap.hide();
			
			// Hide tooltip if shown
			columnTriggerTooltip.hide();
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
		}
		// END - Handling of column show anim end event
		
		// START - Handling of column show anim start event
		function onColumnShowAnimStart(e) {
			
			layout.removeClass('collapsed-filter-nav');
			
			columnFilterSidebarContent.show();
		}
		// END - Handling of column show anim start event
		
		

	}
);
