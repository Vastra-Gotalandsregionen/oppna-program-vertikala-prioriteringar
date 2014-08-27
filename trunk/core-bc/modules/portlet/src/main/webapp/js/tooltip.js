
// Instantiating a Alloy "sandbox"
AUI().ready(
	'anim-base',
	'aui-base',
	'aui-tooltip',
	function(A) {
		var tableHeaderTooltips;

		setupTooltips();
		
		// START - handling of tool tips
		function setupTooltips() {
			
			//var colHeadingSpans = A.all('.main-content table th span');
			var colHeadingSpans = A.all('.enhancedToolTip span');
			
			colHeadingSpans.each(function() {
				// variable this stands for the active node in the loop
				
				var currentTitle = this.get('title');
				
				if(currentTitle != '') {
					// java script is enabled we convert all \n to <br/> in tool-tips for line break
					var newTitle = currentTitle.replace(/\n/g, '<br />');
					currentTitle = newTitle;
					
					this.addClass('vp-tooltip');
					this.set('title', currentTitle);
				}
				
				// Set new title attribute
				this.set('title', currentTitle);
			});
			
            try {
                if (A.all('.enhancedToolTip span.vp-tooltip').size() > 0) {

                    tableHeaderTooltips = new A.TooltipDelegate({
                        trigger: '.enhancedToolTip span.vp-tooltip',
                        align: { points: [ 'tc', 'bc' ] },
                        width: '300px',
                        title: true
                    });
                }
            } catch (eee) {
                if (window['console']) console.log('Initiating tool tips failed.', eee);
                alert('Init toolTips failed with ' + eee.message);
            }
		}
		// END - handling of tool tips
	}
);