require.config({
    // The shim config allows us to configure dependencies for
    // scripts that do not call define() to register a module
    shim: {
        'underscore': {
            exports: '_'
        },
        'backbone': {
            deps: [
                'underscore',
                'jquery'
            ],
            exports: 'Backbone'
        },
        'bootstrap': {
            deps: [
                'jquery'
            ],
            exports: 'bootstrap'
        },
        'ace' : { 
        	exports: 'ace'
        }

    },
    paths: {
        jquery: 'lib/jquery/jquery-1.8.2',
        ace: 'lib/ace/ace',
        underscore: 'lib/backbone/underscore-min',
        backbone: 'lib/backbone/backbone-min',
        bootstrap: 'lib/bootstrap/bootstrap.min',
        text: 'lib/require/text'
    }
});

require([
    'jquery',
    'backbone',
    'ace',
    'views/agentEditorView'
], function( $, Backbone, ace, AgentEditorView) {
    $(function() {
    	
    	var editor = ace.edit("editor");
        editor.setTheme("ace/theme/github");
        editor.getSession().setMode("ace/mode/javascript");

        // Initialize the application view
        var app = new AgentEditorView({ aceEditor : editor });

        // render the application
        app.render();
    });

});