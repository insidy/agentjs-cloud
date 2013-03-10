define([
	'jquery',
	'underscore',
	'backbone'
], function( $, _, Backbone ) {

	var AgentEditorView = Backbone.View.extend({
		
		agentList: [],
		currentAgent: {
				id: null,
				name: "",
				source: ""
		},
		editing: false,

		el: 'body',

		// Compile our template
		//template: _.template( appTemplate ),

		events: {
			'click #btnSearchAgents': 'doSearchAgents',
			'click #btnCreate': 'doCreateAgent',
			'click #btnSave': 'doSaveAgent',
			'click #agent-table > tbody > tr': 'onTableLineClick'
		},


		initialize: function() {
			
		},


		render: function() {
			
			if(this.agentList.length > 0) {
				this.buildAgentTable();
				$("#agent-table").show();
			} else {
				$("#agent-table").hide();
			}
			
			if(this.editing == true) {
				$("#agent-toolbar").show();
			} else {
				$("#agent-toolbar").hide();
			}
			

			return this;
		},
		
		ajaxSave: function(agent) {
			var userId = $("#txtFbName").val(); 
			
			var method = "";
			var url = "";
			if(agent.id) { // change
				method = "PUT";
				url = "rest/agent/" + userId + "/" + agent.id;
			} else { // create
				method = "POST";
				url = "rest/agent/" + userId;
			}
			
			$.ajax({
				type: method,
				url: url,
				contentType: 'application/json',
				context: this,
				data: JSON.stringify(agent),
				dataType: 'json',
				success: function(response) {
					this.doSearchAgents();
					this.editing = false;
					this.render();
				},
				error: function() {
					this.doSearchAgents();
					this.editing = false;
					this.render();
				}
			});
		},
		
		ajaxSearch: function() {
			
			var userId = $("#txtFbName").val(); 
			
			$.ajax({
				type: 'GET',
				url: 'rest/agent/' + userId,
				contentType: 'application/json',
				context: this,
				dataType: 'json',
				success: function(response) {
					this.agentList = [];
					if($.isArray(response.agentJson)) {
						this.agentList = response.agentJson;
					} else {
						this.agentList.push(response.agentJson); // single element
					}
					this.render();
				},
				error: function() {
					this.agentList = [];
					this.render();
				}
			});
		},
		
		buildAgentTable: function() {
			var htmlData = "";
			for(var i = 0; i < this.agentList.length; i++) {
				var agent = this.agentList[i];
				htmlData += "<tr><td data-agent-id=\"" + agent.id + "\">" + agent.name + "</td></tr>";
			}
			$("#agent-table-body").html(htmlData);
		},
		
		doCreateAgent: function(e) {
			this.currentAgent = {
					id: null,
					name: "",
					source: ""
			};
			this.editAgent();
			return false;
		},

		doSearchAgents: function(e) {
			this.ajaxSearch();
			return false;
		},

		doSaveAgent: function(e) {
			this.currentAgent.name = $("#txtAgentName").val();
			this.currentAgent.sourceCode = this.options.aceEditor.getValue();
			this.ajaxSave(this.currentAgent);
			return false;
		},
		
		getAgent: function(agentId) {
			for(var i = 0; i < this.agentList.length; i++) {
				var agent = this.agentList[i];
				if(agent.id == agentId) {
					return agent;
				}
			}
		},
		
		onTableLineClick: function(e) {
			console.log(this);
			this.currentAgent = this.getAgent($(e.target).attr("data-agent-id"));
			this.editAgent();
		},
		
		editAgent: function() {
			this.editing = true;
			$("#txtAgentName").val(this.currentAgent.name);
			this.options.aceEditor.setValue(this.currentAgent.sourceCode);
			this.render();
		}
	});

	return AgentEditorView;
});
