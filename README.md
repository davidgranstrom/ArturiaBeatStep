ArturiaBeatStep
===============

Controller class for Arturia BeatStep, mapped to factory defaults (actual controller defaults, which differ from the data sheet).

Basic usage
-----------

```
a = ArturiaBeatStep();

// register a function to be evaluted when fader1 is changed
a.knob1.onChange = {|val| (val/127).postln };

// overwrite the previous assignment
a.knob1.onChange = {|val| val.linexp(0, 127, 20, 20000).postln };

a.pad1.onPress = { "Hello, ".post };
a.pad1.onRelease = { "Arturia BeatStep!".postln };
```

Incremental assignment
----------------------

It is possible to incrementally assign the knobs and pads.

```
a = ArturiaBeatStep();

a.knobs.do {|knob, i|
    knob.onChange = {|val|
        "Knob % value %\n".postf(i+1, val);
    };
};

a.pads.do {|pad, i|
    pad.onPress = {|val|
        "Pad % pressed %\n".postf(i+1, val);
    };

    pad.onRelease = {|val|
        "Pad % released %\n".postf(i+1, val);
    };
};
```

Interface
---------

### Methods

`onChange` all controls can register a function using this method

`onPress` register pad press

`onRelease` register pad release

`free` unregisters a MIDI responder

`freeAll` unregisters all MIDI responders

*Note: `Cmd-.` removes all MIDI responders by default*

### Controller names

* `knob1 .. 8`
* `pad1 .. 8`

#### Collections

* `knobs`
* `pads`
