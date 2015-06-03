
(function($){

    $.fn.extend({

	    /**
	     * Stores the original version of offset(), so that we don't lose it
	     */
	    _offset : $.fn.offset,

		/**
		 * Set or get the specific left and top position of the matched
		 * elements, relative the the browser window by calling setXY
		 * @param {Object} newOffset
		 */
		offset : function(newOffset){
		return !newOffset ? this._offset() : this.each(function(){
			var el = this;

			var hide = false;

			if($(el).css('display')=='none'){
			    hide = true;
			    $(el).show();
			};

			var style_pos = $(el).css('position');

			// default to relative
			if (style_pos == 'static') {
			    $(el).css('position','relative');
			    style_pos = 'relative';
			};

			var offset = $(el).offset();

			if (offset){
			    var delta = {
				left : parseInt($(el).css('left'), 10),
				top: parseInt($(el).css('top'), 10)
			    };

			    // in case of 'auto'
			    if (isNaN(delta.left))
				delta.left = (style_pos == 'relative') ? 0 : el.offsetLeft;
			    if (isNaN(delta.top))
				delta.top = (style_pos == 'relative') ? 0 : el.offsetTop;

			    if (newOffset.left || newOffset.left===0)
				$(el).css('left',newOffset.left - offset.left + delta.left + 'px');

			    if (newOffset.top || newOffset.top===0)
				$(el).css('top',newOffset.top - offset.top + delta.top + 'px');
			};
			if(hide) $(el).hide();
		    });
	    }

	});

})(jQuery);

