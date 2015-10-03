// ===========================================================================
// Title         : ArturiaBeatStep
// Description   : Controller class for Arturia BeatStep
// Version       : 1.0alpha
// Copyright (c) : David Granström 2015
// ===========================================================================

ArturiaBeatStep {
    var <knobs, <pads;

    var ctls;
    var knobValues, padValues;

    *new {
        ^super.new.init;
    }

    init {
        ctls = ();

        knobs = List[];
        pads  = List[];

        knobValues = [ 10, 74, 71, 76, 77, 93, 73, 75, 114, 18, 19, 16, 17, 91, 79, 72 ];
        padValues  = [ 44, 45, 46, 47, 48, 49, 50, 51, 36, 37, 38, 39, 40, 41, 42, 43 ];

        MIDIClient.init;
        MIDIIn.connectAll;

        this.assignCtls;
    }

    assignCtls {
        knobValues.do {|cc, i|
            var key  = ("knob" ++ (i+1)).asSymbol;
            var knob = ABSKnob(key, cc);
            knobs.add(knob);
            ctls.put(key, knob);
        };

        padValues.collect {|note, i|
            var key = ("pad" ++ (i+1)).asSymbol;
            var pad = ABSPad(key, note);
            pads.add(pad);
            ctls.put(key, pad);
        };
    }

    freeAll {
        ctls.do(_.free);
    }

    doesNotUnderstand {|selector ... args|
        ^ctls[selector] ?? { ^super.doesNotUnderstand(selector, args) }
    }
}

ABSKnob {
    var key, cc;

    *new {|key, cc|
        ^super.newCopyArgs(("abs_" ++ key).asSymbol, cc);
    }

    onChange_ {|func|
        MIDIdef.cc(key, func, cc);
    }

    free {
        MIDIdef.cc(key).free;
    }
}

ABSPad {
    var key, note;

    *new {|key, note|
        ^super.newCopyArgs("abs_" ++ key, note);
    }

    onPress_ {|func|
        MIDIdef.noteOn((key ++ "_on").asSymbol, {|val| func.(val) }, note);
    }

    onRelease_ {|func|
        MIDIdef.noteOff((key ++ "_off").asSymbol, {|val| func.(val) }, note);
    }

    onChange_ {|func|
        MIDIdef.noteOn((key ++ "_on_change").asSymbol, {|val| func.(val) }, note);
        MIDIdef.noteOff((key ++ "_off_change").asSymbol, {|val| func.(val) }, note);
    }

    free {
        var labels = [ "_on", "_off", "_on_change", "_off_change" ];

        labels.do {|label|
            var k = (key ++ label).asSymbol;
            MIDIdef.cc(k).free;
        };
    }
}
