# JustJava
initial commit

**This app is the part of Google India Challenge Scholarship for Android Basics Track Lesson 11.**

- I have used _TextInputLayout_ and _TextInputEditText_ instead of _EditText_.

- I have used _setError(charSequence error)_ to show if the user name is empty

- I have used _String.format()_ to some places for string concatenation.

- I have taken few global variables to avoid messing up things in inappropriate methods. 

For an example, I wanted **instant and updated presentation of order summary** so that user can see the change on the spot. 

That said, I could not decide whether would it be better to create new variables for checkboxes and set listeners on it 
**every time the _calculatePrice()_ method is called** or not than using global variables for them? 
It is something like **Using information through: Global variable Vs Method argument**. 
Though I know it all relate to memory consumption, I could not get idle explanation as each scenario can have different requirement 
but It would be great to hear from the experienced mentors.

- I wanted to clear and reset all input only if the user actually sends an email. 
Hence, I have used _startActivityForResult(Intent intent, int code)_ instead of _startActivity(Intent intent)_ 
with _onActivityResult(int requestCode, int resultCode, Intent data)_.

- I have also implemented Localization concept. I have added "Gujarati" language support in addition to native english language.

- I wanted better end user experience in terms of increasing or decreasing quantity. 
So, I have used _onTouchListener(View v)_ method along with custom runnable.

- I was trying to give screen orientation support but as I have already implemented 
_onCheckedChanged(CompoundButton buttonView, boolean isChecked)_ and _addTextChangedListener(TextWatcher watcher)_ those are being used for instant and updated presentation of order summary to end user, I could not find the necessity of screen orientation support for the current case.
