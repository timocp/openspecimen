var openspecimen = openspecimen || {}
openspecimen.ui = openspecimen.ui || {};
openspecimen.ui.fancy = openspecimen.ui.fancy || {};

openspecimen.ui.fancy.SpecimenPosition = function(params) {
  this.inputEl = null;

  this.validator;

  var field = params.field;

  var id = params.id;

  var that = this;

  this.minWidth = 400;

  this.render = function() {
    this.specimenId = params.args.appData.objectId;

    this.containerPositionsEl = addModalToDom();

    this.inputEl = $("<div/>").addClass("clearfix");

    this.containerEl = $("<div/>")
      .css("width", "50%")
      .css("float", "left")
      .css("padding-right", "15px");
    this.inputEl.append(this.containerEl);

    var p1 = $.extend({searchFilters: {specimenId: this.specimenId}}, params);
    p1.field = {
      name: 'storageContainer'
    };
    this.storageContainer = new openspecimen.ui.fancy.StorageContainer(p1, clearPositions);
    this.containerEl.append(this.storageContainer.render());


    this.posEl = $("<div/>")
      .css("width", "30%")
      .css("float", "left")
      .css("padding-right", "10px");
    this.inputEl.append(this.posEl);

    this.posXEl = $("<div/>")
      .css("width", "50%")
      .css("float", "left")
      .css("padding-right", "5px");
    this.posEl.append(this.posXEl);

    var posXId = 'de-pos-x-' + edu.common.de.nextUid();
    this.posX = new edu.common.de.TextField(posXId, {name: 'posX'});
    this.posXEl.append(this.posX.render());

    this.posYEl = $("<div/>")
      .css("width", "50%")
      .css("float", "left")
      .css("padding-right", "5px");
    this.posEl.append(this.posYEl);

    var posYId = 'de-pos-y-' + edu.common.de.nextUid();
    this.posY = new edu.common.de.TextField(posYId, {name: 'posY'});
    this.posYEl.append(this.posY.render());

    this.lookupEl = $("<div/>")
      .css("width", "20%")
      .css("float", "left")
    this.inputEl.append(this.lookupEl);

    this.lookupBtn = $("<button/>")
      .addClass("btn btn-default")
      .css("width", "100%")
      .append("Lookup");
    this.lookupEl.append(this.lookupBtn);

    
    this.lookupBtn.on('click', function() {
      if (!that.storageContainer.getValue().value) {
        alert("Virtual storage containers do not have positions map");
        return;
      }

      that.containerPositionsEl.attr(
        {
          'data-specimen-id': that.specimenId,
          'data-container-id': that.storageContainer.getValue().value,
          'data-pos-x': posXId,
          'data-pos-y': posYId
        }
      ).modal('show');
    });

    return this.inputEl;
  };

  this.postRender = function() {
    if (!this.posId) {
      $.ajax({
        type: 'GET',
        url: '/openspecimen/rest/ng/specimens/' + this.specimenId + '/position'
      }).done(function(position) {
        var value = {storageContainer: -1};
        if (position) {
          value = {
            storageContainer: position.storageContainerId,
            posX: position.posX,
            posY: position.posY,
            id: position.id
          };
        }

        that.setValue(that.recId, value);
        that.storageContainer.postRender();
      }).fail(function() {
        alert("Failed to load specimen position for specimen: " + that.specimenId);
      });
    } else {
      this.storageContainer.postRender();
    }
  };

  this.getName = function() {
    return field.name;
  };

  this.getCaption = function() {
    return field.caption;
  };

  this.getTooltip = function() {
    return field.toolTip ? field.toolTip : field.caption;
  };
	  
  this.getValue = function(clone) {
    var value = {
      storageContainer: this.storageContainer.getValue().value,
      posX: this.posX.getValue().value,
      posY: this.posY.getValue().value,
      appData: {
        specimenId: params.args.appData.objectId
      }
    };

    if (!clone) { // when cloning, we do not want to copy position identifiers
      value.id = this.posId;
    }

    return {name: field.name, value: value};
  };

  this.setValue = function(recId, value) {
    this.recId = recId;
    if (!value) {
      return;
    }

    if (!this.posId && !!value.id) { // to make it work for apply first to all
      this.posId = value.id;
    }

    this.storageContainer.setValue(recId, value.storageContainer);
    this.posX.setValue(recId, value.posX);
    this.posY.setValue(recId, value.posY);
  };

  this.getDisplayValue = function() {
    var displayVal = this.storageContainer.getDisplayValue().value;
    if (displayVal) {
      displayVal += " (" + this.posX.getDisplayValue().value + ", " + this.posY.getDisplayValue().value + ")";
    }

    return {
      name: field.name, 
      value: displayVal
    }
  };
  
  this.validate = function() {
    //return this.validator.validate();
    return true;
  };

  this.getPrintEl = function() {
    return edu.common.de.Utility.getPrintEl(this);
  };


  function clearPositions() {
    that.posX.setValue(that.recId, "");
    that.posY.setValue(that.recId, "");
  };

  function addModalToDom() {
    var modalEl = $("#de-storage-container-positions-grid-modal");
    if (modalEl.length) {
      return modalEl;
    }

    var modalEl = getModalEl();
    $('body').append(modalEl);

    var modalData = {el: modalEl, grid: null};
    modalEl.find('#de-save-specimen-position').on('click', function() {
      var selectedPos = modalData.grid.getSelectedPos();
      $('#' + modalEl.attr('data-pos-x')).val(selectedPos.posX);
      $('#' + modalEl.attr('data-pos-y')).val(selectedPos.posY);

      modalEl.modal('hide');
    });

    modalEl.on('show.bs.modal', function(event) {
      showContainerPositionsGrid(modalData);
    });

    modalEl.on('hide.bs.modal', function(event) {
      modalData.grid.destroy();
      modalData.grid = null;
    });

    return modalEl;
  }

  function getModalEl() {
    var modalHtml = 
      '<div class="modal fade" id="de-storage-containers-positions" tabindex="-1" role="dialog">' +
        '<div class="modal-dialog">' +
          '<div class="modal-content">' +
            '<div class="ka-modal-header">' +
              '<h4 class="pull-left modal-title">Container Positions</h4>' +
              '<button type="button" class="ka-close" data-dismiss="modal" aria-hidden="true">&times;</button>' +
            '</div>' +
            '<div class="ka-modal-body">' +
              '<p>Showing positions map for: <span id="de-storage-container-name"></span></p>' +
              '<div class="form-group">' +
                '<div id="de-storage-container-positions-grid" style="max-height:350px;max-width:600px;overflow:auto;border: 1px solid #ddd">' +
                '</div>' +
              '</div>' +
              '<div class="form-group">' +
                '<label>Selected Position: </label>' +
                '<span id="de-selected-container-pos"></span>' +
              '</div>' +
              '<div class="ka-modal-footer">' +
                '<button type="button" class="btn ka-btn-text" data-dismiss="modal">Close</button>' +
                '<button id="de-save-specimen-position" type="button" class="btn btn-primary">Done</button>' +
              '</div>' +
            '</div>' +
          '</div>' +
        '</div>' +
      '</div>';

    return $(modalHtml);
  };

  function showContainerPositionsGrid(modalData) {
    var containerId = modalData.el.attr('data-container-id');
    $.ajax({
      type: 'GET', 
      url: '/openspecimen/rest/ng/storage-containers/' + containerId,
      data: {includeOccupiedPositions: true}
    }).done(function(containerInfo) {
      modalData.el.find("#de-storage-container-name").text(containerInfo.name);
      modalData.grid = new openspecimen.ui.fancy.ContainerPositionsGrid(
        modalData.el.find("#de-storage-container-positions-grid"),
        modalData.el.find("#de-selected-container-pos"),
        {},
        containerInfo
      ).render();
    }).fail(function() {
       alert("Failed to load container occupancy grid");
    });
  };
};

openspecimen.ui.fancy.ContainerPositionsGrid = function(parentEl, posTextEl, inputPos, containerInfo) {
  var Utility = openspecimen.ui.fancy.SpecimenPositionUtility;

  this.selectedPos = $.extend({}, inputPos);

  var convertersMap = {
    'Numbers'             : Utility.toNum,
    'Alphabets Upper Case': Utility.toUpperAlphabet,
    'Alphabets Lower Case': Utility.toLowerAlphabet,
    'Roman Upper Case'    : Utility.toUpperRoman,
    'Roman Lower Case'    : Utility.toLowerRoman
  };

  function getGridHead() {
    var dimensionLabelFmt = convertersMap[containerInfo.dimensionOneLabelingScheme];

    var thead = $("<thead/>");
    var tr = $("<tr/>");
    tr.append($("<th/>").addClass("os-container-pos").append("&nbsp;"));
    for (var i = 0; i < +containerInfo.dimensionOneCapacity; ++i) {
      tr.append($("<th/>").addClass("os-container-pos").append(dimensionLabelFmt(i + 1)));
    }

    thead.append(tr);
    return thead;
  }

  function getOccupiedPositionsMap(occupied) {
    var occupiedPosMap = {};
    for (var i = 0; i < occupied.length; ++i) {
      occupiedPosMap[occupied[i]] = true;
    }

    return occupiedPosMap;
  }

  function getGridBody() {
    var occupiedPosMap = getOccupiedPositionsMap(containerInfo.occupiedPositions);

    var dimOneLabelFmt = convertersMap[containerInfo.dimensionOneLabelingScheme];
    var dimTwoLabelFmt = convertersMap[containerInfo.dimensionTwoLabelingScheme];

    var tbody = $("<tbody/>");
    for (var i = 0; i < +containerInfo.dimensionTwoCapacity; ++i) {
      var posY = dimTwoLabelFmt(i + 1);
      var tr = $("<tr/>")
        .append($("<th/>").addClass("os-container-pos").append(posY));

      for (var j = 0; j < +containerInfo.dimensionOneCapacity; ++j) {
        var posX = dimOneLabelFmt(j + 1);
        var td = $("<td/>")
          .addClass("os-container-pos")
          .attr({ 'data-pos-x': posX, 'data-pos-y': posY, 'title': '(' + posX + ', ' + posY + ')'});

        var pos = i * containerInfo.dimensionOneCapacity + j + 1;
        if (occupiedPosMap[pos]) {
          td.addClass("occupied");
        }

        td.append($("<span/>").addClass("os-circle"));

        if (this.selectedPos && this.selectedPos.posX == posX && this.selectedPos.posY == posY) {
          td.addClass("selected");
          posTextEl.text("(" + this.selectedPos.posX + ", " + this.selectedPos.posY + ")");
        }

        tr.append(td);
      }

      tbody.append(tr);
    }

    return tbody;
  }
    
  function listenForSelections(table, selectedPos) {
    table.find("td.os-container-pos:not(.occupied)").on("click", function(event) {
      var posEl = $(event.currentTarget);
      
      if (posEl.hasClass("selected")) {
        posEl.removeClass("selected");
        posTextEl.text("None");
      } else {
        table.find("td.os-container-pos.selected").removeClass("selected");
        posEl.addClass("selected");
        selectedPos.posX = posEl.attr('data-pos-x');
        selectedPos.posY = posEl.attr('data-pos-y');
        posTextEl.text("(" + selectedPos.posX + ", " + selectedPos.posY + ")");
      }
    });
  }

  this.render = function() {
    var table = $("<table/>").addClass("table table-bordered")
      .append(getGridHead())
      .append(getGridBody()); 

    parentEl.append(table);
    listenForSelections(table, this.selectedPos); 
    return this;
  };

  this.destroy = function() {
    parentEl.children().remove();
    posTextEl.text("");
    return this;
  };

  this.getSelectedPos = function() {
    return this.selectedPos;
  };
};

openspecimen.ui.fancy.SpecimenPositionUtility = {
  toAlphabet: function(num, aCharCode) {
    var result = "";
    while (num > 0) {
      result = String.fromCharCode((num - 1) % 26 + aCharCode) + result;
      num = Math.floor((num - 1) / 26);
    }

    return result;
  },

  toRoman: function(num, upper) {
    var lookup = {
      M: 1000, CM: 900,
      D:  500, CD: 400,
      C:  100, XC:  90,
      L:   50, XL:  40,
      X:   10, IX:   9,
      V:    5, IV:   4,
      I:    1
    };

    var result = '';
    for (var i in lookup) {
      while (num >= lookup[i]) {
        result += i;
        num -= lookup[i];
      }
    }
 
    return upper ? result : result.toLowerCase();
  },

  toNum: function(num) {
    return num;
  },

  toUpperAlphabet: function(num) {
    return openspecimen.ui.fancy.SpecimenPositionUtility.toAlphabet(num, 'A'.charCodeAt(0));
  },

  toLowerAlphabet: function(num) {
    return openspecimen.ui.fancy.SpecimenPositionUtility.toAlphabet(num, 'a'.charCodeAt(0));
  },

  toUpperRoman: function(num) {
    return openspecimen.ui.fancy.SpecimenPositionUtility.toRoman(num, true);
  },

  toLowerRoman: function(num) {
    return openspecimen.ui.fancy.SpecimenPositionUtility.toRoman(num, false);
  }
};

edu.common.de.FieldManager.getInstance()
  .register({
    name: "specimenPosition",
    displayName: "Specimen Position",
    fieldCtor: openspecimen.ui.fancy.SpecimenPosition
  });
