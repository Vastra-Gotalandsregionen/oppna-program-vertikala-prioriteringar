
// Instantiating a Alloy "sandbox"
AUI().ready(
	'anim-base',
	'aui-base',
	'aui-tooltip',
	function(A) {

		var tableHeaderTooltips, columnTriggerTooltip;
		var columnHideTrigger, columnShowTrigger, columnFilterNav, columnFilterNavContent;
		var columnShowAnim, columnHideAnim;
	
		setupTooltips();
		setupColumnToggle();
		
		// START - handling of tool tips
		function setupTooltips() {
			
			var colHeadingSpans = A.all('.main-content table th span');
			
			colHeadingSpans.each(function() {
				// variable this stands for the active node in the loop
				
				var currentTitle = this.get('title');
				
				if(currentTitle != '') {
					// java script is enabled we convert all \n to <br/> in tool-tips for line break
					var newTitle = currentTitle.replace(/\n/g, '<br />');
					currentTitle = newTitle;
					
					this.addClass('tooltip');
					this.set('title', currentTitle);
				}
				
				// Set new title attribute
				this.set('title', currentTitle);
			});
			
			tableHeaderTooltips = new A.Tooltip({
				trigger: '.main-content table th span.tooltip',
				align: { points: [ 'tc', 'bc' ] },
				width: '300px',
				title: true
			}).render();
			
		}
		// END - handling of tool tips
		
		// START - Column toggle
		function setupColumnToggle() {
			
			columnFilterNav = A.one('#filterNav'); // detect via id
			columnFilterNavContent = A.one('.filter-sidebar');
			columnHideTrigger = A.one('.column-control-hide');  // detect via class
			columnShowTrigger = A.one('.column-control-show');
			
			// If there is no columnFilterNaw - don't proceed
			if(!columnFilterNav) { return; }
			
			columnHideTrigger.on('click', onColumnHideClick);
			columnShowTrigger.on('click', onColumnShowClick);
			
			// Hide show trigger
			columnShowTrigger.hide();
			
			// Define animations to be used when showing or hiding columns
			
		    columnShowAnim = new A.Anim({
		    	node: columnFilterNav,
		    	to: { width: '300px' }
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
		}
		// END - Handling of column hide anim start event
		
		
		// START - Handling of column hide anim start event
		function onColumnHideAnimStart(e) {
			
			// Hide content when animation starts
			columnFilterNavContent.hide();
			
			// Hide tooltip if shown
			columnTriggerTooltip.hide();
		}
		// END - Handling of column hide anim start event
		
		// START - Handling of column show anim end event
		function onColumnShowAnimEnd(e) {
			
			// Show content when animation ends
			columnFilterNavContent.show();
			
			// Toggle triggers
			columnShowTrigger.hide();
			columnHideTrigger.show();
			
			// Hide tooltip if shown
			columnTriggerTooltip.hide();
		}
		// END - Handling of column show anim end event
		
		// START - Handling of column show anim start event
		function onColumnShowAnimStart(e) {
			
			// Hide content when animation starts
			columnFilterNavContent.hide();
		}
		// END - Handling of column show anim start event
		
		

	}
);
