document.getElementById("submit").addEventListener("click", () => alert("The form has been submitted"));;

let stateOptionsList = document.getElementById('inputState').options;
let stateOptions = [{
        text: 'AL',
        value: 'AL'
    },
    {
        text: 'AK',
        value: 'AK'
    },
    {
        text: 'AZ',
        value: 'AZ'
    },
    {
        text: 'AZ',
        value: 'AZ'
    },
    {
        text: 'AR',
        value: 'AR'
    },
    {
        text: 'CA',
        value: 'CA'
    },
    {
        text: 'CO',
        value: 'CO'
    },
    {
        text: 'CT',
        value: 'CT'
    },
    {
        text: 'DE',
        value: 'DE'
    },
    {
        text: 'FL',
        value: 'FL'
    },
    {
        text: 'GA',
        value: 'GA'
    },
    {
        text: 'HI',
        value: 'HI'
    },
    {
        text: 'ID',
        value: 'ID'
    },
    {
        text: 'IL',
        value: 'IL'
    },
    {
        text: 'IN',
        value: 'IN'
    },
    {
        text: 'IA',
        value: 'IA'
    },
    {
        text: 'KS',
        value: 'KS'
    },
    {
        text: 'KY',
        value: 'KY'
    },
    {
        text: 'LA',
        value: 'LA'
    },
    {
        text: 'ME',
        value: 'ME'
    },
    {
        text: 'MD',
        value: 'MD'
    },
    {
        text: 'MA',
        value: 'MA'
    },
    {
        text: 'MI',
        value: 'MI'
    },
    {
        text: 'MN',
        value: 'MN'
    },
    {
        text: 'MS',
        value: 'MS'
    },
    {
        text: 'MO',
        value: 'MO'
    },
    {
        text: 'MT',
        value: 'MT'
    },
    {
        text: 'NE',
        value: 'NE'
    },
    {
        text: 'NV',
        value: 'NV'
    },
    {
        text: 'NH',
        value: 'NH'
    },
    {
        text: 'NJ',
        value: 'NJ'
    },
    {
        text: 'NM',
        value: 'NM'
    },
    {
        text: 'NY',
        value: 'NY'
    },
    {
        text: 'NC',
        value: 'NC'
    },
    {
        text: 'ND',
        value: 'ND'
    },
    {
        text: 'OH',
        value: 'OH'
    },
    {
        text: 'OK',
        value: 'OK'
    },
    {
        text: 'OR',
        value: 'OR'
    },
    {
        text: 'PA',
        value: 'PA'
    },
    {
        text: 'RI',
        value: 'RI'
    },
    {
        text: 'SC',
        value: 'SC'
    },
    {
        text: 'SD',
        value: 'SD'
    },
    {
        text: 'TN',
        value: 'TN'
    },
    {
        text: 'TX',
        value: 'TX'
    },
    {
        text: 'UT',
        value: 'UT'
    },
    {
        text: 'VT',
        value: 'VT'
    },
    {
        text: 'VA',
        value: 'VA'
    },
    {
        text: 'WA',
        value: 'WA'
    },
    {
        text: 'WV',
        value: 'WV'
    },
    {
        text: 'WI',
        value: 'WI'
    },
    {
        text: 'WY',
        value: 'WY'
    },
]

stateOptionsList.add(new Option('Choose...', null, true))
stateOptions.forEach(state =>
    stateOptionsList.add(
        new Option(state.text, state.value, state.selected)
    )
);

console.log(stateOptionsList.length)