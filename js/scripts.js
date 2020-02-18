document.getElementById("submitBatch").addEventListener("click", () => {
    file = document.getElementById("batchFileUpload").files;
    console.log(file);
    alert(file.length > 0 ? 'Uploaded file: ' + file[0].name : 'No files uploaded');
});

document.getElementById("addressFormLink").addEventListener("click", () => {
    document.getElementById("addressFormLink").className = "nav-item active"
    document.getElementById("batchImportLink").className = "nav-item"
    document.getElementById("addressForm").style.display = "block"
    document.getElementById("batchForm").style.display = "none"
});

document.getElementById("batchImportLink").addEventListener("click", () => {
    document.getElementById("batchImportLink").className = "nav-item active"
    document.getElementById("addressFormLink").className = "nav-item"
    document.getElementById("addressForm").style.display = "none"
    document.getElementById("batchForm").style.display = "block"
});

document.getElementById("submit").addEventListener("click", () => {
    // fetch results from geocoder
    let jsonResponse;
    console.log(document.getElementById('inputState').value)
    let url = new URL('https://geocoding.geo.census.gov/geocoder/locations/address?'),
        addressparams = {
            street: document.getElementById("inputAddress1").value,
            city: document.getElementById("inputCity").value,
            state: document.getElementById('inputState').value,
            zip: document.getElementById('inputZip').value,
            benchmark: 'Public_AR_Census2010',
            'format': 'JSON'
        }
    Object.keys(addressparams).forEach(key => url.searchParams.append(key, addressparams[key]))
    fetch(url)
        .then((response) => {
            return response.json();
        })
        .then((myJson) => {
            // console.log(myJson);
            jsonResponse = myJson;
            //will need to do some checks here: 
            x = myJson.result.addressMatches[0].coordinates.x
            y = myJson.result.addressMatches[0].coordinates.y
            let confirmation = "The address coordinates of (" + x.toString() + ", " + y.toString() + ") has been geocoded."
            $('#modal').find(".modal-body").text(confirmation)
            $('#modal').modal('show');
        })
        .catch(err => {
            console.log(err);
            let confirmation = "Error please fix"
            $('#modal').find(".modal-body").text(confirmation)
            $('#modal').modal('show');
        });

    // TODO: replace with results from geocoder

});

(function() {
    'use strict';
    window.addEventListener('load', function() {
        // init bootstrap custom file upload
        bsCustomFileInput.init();
        // hide batch import tab
        document.getElementById("batchForm").style.display = "none"
            // Fetch all the forms we want to apply custom Bootstrap validation styles to
        var forms = document.getElementsByClassName('needs-validation');
        // Loop over them and prevent submission
        var validation = Array.prototype.filter.call(forms, function(form) {
            form.addEventListener('submit', function(event) {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    }, false);
})();

let stateOptionsList = document.getElementById('inputState').options;
let stateOptions = [{
        "name": "Alabama",
        "abbreviation": "AL"
    },
    {
        "name": "Alaska",
        "abbreviation": "AK"
    },
    {
        "name": "American Samoa",
        "abbreviation": "AS"
    },
    {
        "name": "Arizona",
        "abbreviation": "AZ"
    },
    {
        "name": "Arkansas",
        "abbreviation": "AR"
    },
    {
        "name": "California",
        "abbreviation": "CA"
    },
    {
        "name": "Colorado",
        "abbreviation": "CO"
    },
    {
        "name": "Connecticut",
        "abbreviation": "CT"
    },
    {
        "name": "Delaware",
        "abbreviation": "DE"
    },
    {
        "name": "District Of Columbia",
        "abbreviation": "DC"
    },
    {
        "name": "Federated States Of Micronesia",
        "abbreviation": "FM"
    },
    {
        "name": "Florida",
        "abbreviation": "FL"
    },
    {
        "name": "Georgia",
        "abbreviation": "GA"
    },
    {
        "name": "Guam",
        "abbreviation": "GU"
    },
    {
        "name": "Hawaii",
        "abbreviation": "HI"
    },
    {
        "name": "Idaho",
        "abbreviation": "ID"
    },
    {
        "name": "Illinois",
        "abbreviation": "IL"
    },
    {
        "name": "Indiana",
        "abbreviation": "IN"
    },
    {
        "name": "Iowa",
        "abbreviation": "IA"
    },
    {
        "name": "Kansas",
        "abbreviation": "KS"
    },
    {
        "name": "Kentucky",
        "abbreviation": "KY"
    },
    {
        "name": "Louisiana",
        "abbreviation": "LA"
    },
    {
        "name": "Maine",
        "abbreviation": "ME"
    },
    {
        "name": "Marshall Islands",
        "abbreviation": "MH"
    },
    {
        "name": "Maryland",
        "abbreviation": "MD"
    },
    {
        "name": "Massachusetts",
        "abbreviation": "MA"
    },
    {
        "name": "Michigan",
        "abbreviation": "MI"
    },
    {
        "name": "Minnesota",
        "abbreviation": "MN"
    },
    {
        "name": "Mississippi",
        "abbreviation": "MS"
    },
    {
        "name": "Missouri",
        "abbreviation": "MO"
    },
    {
        "name": "Montana",
        "abbreviation": "MT"
    },
    {
        "name": "Nebraska",
        "abbreviation": "NE"
    },
    {
        "name": "Nevada",
        "abbreviation": "NV"
    },
    {
        "name": "New Hampshire",
        "abbreviation": "NH"
    },
    {
        "name": "New Jersey",
        "abbreviation": "NJ"
    },
    {
        "name": "New Mexico",
        "abbreviation": "NM"
    },
    {
        "name": "New York",
        "abbreviation": "NY"
    },
    {
        "name": "North Carolina",
        "abbreviation": "NC"
    },
    {
        "name": "North Dakota",
        "abbreviation": "ND"
    },
    {
        "name": "Northern Mariana Islands",
        "abbreviation": "MP"
    },
    {
        "name": "Ohio",
        "abbreviation": "OH"
    },
    {
        "name": "Oklahoma",
        "abbreviation": "OK"
    },
    {
        "name": "Oregon",
        "abbreviation": "OR"
    },
    {
        "name": "Palau",
        "abbreviation": "PW"
    },
    {
        "name": "Pennsylvania",
        "abbreviation": "PA"
    },
    {
        "name": "Puerto Rico",
        "abbreviation": "PR"
    },
    {
        "name": "Rhode Island",
        "abbreviation": "RI"
    },
    {
        "name": "South Carolina",
        "abbreviation": "SC"
    },
    {
        "name": "South Dakota",
        "abbreviation": "SD"
    },
    {
        "name": "Tennessee",
        "abbreviation": "TN"
    },
    {
        "name": "Texas",
        "abbreviation": "TX"
    },
    {
        "name": "Utah",
        "abbreviation": "UT"
    },
    {
        "name": "Vermont",
        "abbreviation": "VT"
    },
    {
        "name": "Virgin Islands",
        "abbreviation": "VI"
    },
    {
        "name": "Virginia",
        "abbreviation": "VA"
    },
    {
        "name": "Washington",
        "abbreviation": "WA"
    },
    {
        "name": "West Virginia",
        "abbreviation": "WV"
    },
    {
        "name": "Wisconsin",
        "abbreviation": "WI"
    },
    {
        "name": "Wyoming",
        "abbreviation": "WY"
    }
]

stateOptionsList.add(new Option('Choose...', null, true))
stateOptions.forEach(state =>
    stateOptionsList.add(
        new Option(state.name, state.abbreviation, state.selected)
    )
);



console.log(stateOptionsList.length)