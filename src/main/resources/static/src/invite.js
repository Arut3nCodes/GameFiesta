
function toggleContent() {
    const invitesContainer = document.querySelector('.invites-container');
    const invitesList = invitesContainer.querySelector('.invites-list');

    invitesContainer.classList.toggle('active');

    if (invitesContainer.classList.contains('active')) {
        fetchInvites();
        fetchTournamentInvites();
    } else {
        invitesList.innerHTML = '';
    }
}

function fetchInvites() {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/getInvites', true);
    xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    xhr.onload = function () {
        if (xhr.status === 200) {
            var teams = JSON.parse(xhr.responseText);
            renderInvites(teams);
        }
    };
    xhr.send();

}

function fetchTournamentInvites(){
    
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/getTournamentInvites', true);
    xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    xhr.onload = function () {
        if (xhr.status === 200) {
            var teams = JSON.parse(xhr.responseText);
            renderTournamentInvites(teams);
        }
    };
    xhr.send();

}



function renderInvites(teams) {
    var invitesList = document.getElementById('invitesList');

    if (teams.length > 0) {
        teams.forEach(function (team) {
            var inviteItem = document.createElement('li');
            inviteItem.className = 'invite-item';

            // Customize how each team invite is displayed
            var teamNameElement = document.createElement('span');
            teamNameElement.textContent = 'Team: ' + team.name;
            inviteItem.appendChild(teamNameElement);

            var acceptButton = document.createElement('button');
            acceptButton.textContent = 'Accept';
            acceptButton.onclick = function () {
                acceptInvite(team._id);
            };
            inviteItem.appendChild(acceptButton);

            var rejectButton = document.createElement('button');
            rejectButton.textContent = 'Reject';
            rejectButton.onclick = function () {
                rejectInvite(team._id);
            };
            inviteItem.appendChild(rejectButton);

            invitesList.appendChild(inviteItem);
        });
    } else {
        // Display a message when there are no team invites
        var noInvitesMessage = document.createElement('li');
        noInvitesMessage.textContent = 'No team invites available';
        invitesList.appendChild(noInvitesMessage);
    }
}

function renderTournamentInvites(tournaments) {
    var invitesList = document.getElementById('invitesList');

    if (tournaments.length > 0) {
        tournaments.forEach(function (tournament) {
            var inviteItem = document.createElement('li');
            inviteItem.className = 'invite-item';

            // Customize how each team invite is displayed
            var tournamentNameElement = document.createElement('span');
            tournamentNameElement.textContent = 'Tournament: ' + tournament.title;
            inviteItem.appendChild(tournamentNameElement);

            var acceptButton = document.createElement('button');
            acceptButton.textContent = 'Accept';
            acceptButton.onclick = function () {
                acceptTournamentInvite(team._id);
            };
            inviteItem.appendChild(acceptButton);

            var rejectButton = document.createElement('button');
            rejectButton.textContent = 'Reject';
            rejectButton.onclick = function () {
                rejectInvite(team._id);
            };
            inviteItem.appendChild(rejectButton);

            invitesList.appendChild(inviteItem);
        });
    } else {
        // Display a message when there are no team invites
        var noInvitesMessage = document.createElement('li');
        noInvitesMessage.textContent = 'No tournament invites available';
        invitesList.appendChild(noInvitesMessage);
    }
}

function acceptInvite(teamId) {

    var xhrInv = new XMLHttpRequest();
    xhrInv.open('POST', '/joinTeam', true);
    xhrInv.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');

    xhrInv.onload = function(){
        if(xhrInv.status === 200){
            console.log(xhrInv.responseText)
        }
    }
    xhrInv.send('teamID=' + encodeURIComponent(teamId));
    console.log('Accepted invite to team:', teamId);
}


function acceptTournamentInvite(tournamentId){

}

function rejectInvite(teamId) {
    // Implement logic to reject the invite
    // You can make an Ajax request or handle it based on your requirements
    console.log('Rejected invite to team:', teamId);
}