package com.example.gaebal_saebal_aos_ver2

class GithubRepoData (
    var record_github_repo_name: String, // repo name
    var record_github_repo_fullname: String, // repo full name
    var record_github_repo_owner: String, // repo owner
)

class GithubData (
    var record_github_type: String, // 깃허브 타입: issue, commit, Pull request
    var record_github_date: String, // 깃허브 날짜
    var record_github_title: String, // 깃허브 제목
)