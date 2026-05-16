# Git Commands Guide

This guide covers Git commands from beginner to advanced, with clear explanations and examples. Use it as a reference for version control in your projects.

---

## 1. Git Basics

### 1.1 Install and Setup
- Install Git: https://git-scm.com/downloads
- Configure name and email:
  ```bash
git config --global user.name "Your Name"
git config --global user.email "you@example.com"
```
- Verify configuration:
  ```bash
git config --global --list
```

### 1.2 Initialize a Repository
- Create a new repository in the current folder:
  ```bash
git init
```
- Clone an existing repository:
  ```bash
git clone https://github.com/username/repo.git
```

### 1.3 Check Status and Differences
- Show current status:
  ```bash
git status
```
- View unstaged changes:
  ```bash
git diff
```
- View changes staged for commit:
  ```bash
git diff --staged
```

### 1.4 Add and Commit
- Stage a file:
  ```bash
git add file.txt
```
- Stage all changes:
  ```bash
git add .
```
- Commit staged changes:
  ```bash
git commit -m "Add feature or fix bug"
```
- Amend last commit message or add more changes:
  ```bash
git commit --amend -m "Updated commit message"
```

### 1.5 View History
- Show commit history:
  ```bash
git log
```
- Compact log:
  ```bash
git log --oneline --graph --decorate --all
```
- Show history for one file:
  ```bash
git log -- file.txt
```

---

## 2. Branching and Merging

### 2.1 Branch Commands
- List branches:
  ```bash
git branch
```
- Create a branch:
  ```bash
git branch feature-name
```
- Switch branches:
  ```bash
git checkout feature-name
```
- Create and switch:
  ```bash
git checkout -b feature-name
```

### 2.2 Merging Branches
- Merge a branch into current branch:
  ```bash
git merge feature-name
```
- Merge using fast-forward only:
  ```bash
git merge --ff-only feature-name
```
- Resolve conflicts:
  1. Open conflicted files
  2. Fix conflicts
  3. Stage and commit:
     ```bash
git add conflicted-file.txt
git commit
```

### 2.3 Branch Deletion
- Delete a local branch:
  ```bash
git branch -d feature-name
```
- Force delete:
  ```bash
git branch -D feature-name
```

---

## 3. Working with Remote Repositories

### 3.1 Remote Basics
- List remotes:
  ```bash
git remote -v
```
- Add a new remote:
  ```bash
git remote add origin https://github.com/username/repo.git
```
- Remove a remote:
  ```bash
git remote remove origin
```

### 3.2 Fetch and Pull
- Fetch changes from remote:
  ```bash
git fetch origin
```
- Pull changes and merge:
  ```bash
git pull origin main
```
- Pull with rebase:
  ```bash
git pull --rebase origin main
```

### 3.3 Push Changes
- Push current branch:
  ```bash
git push origin feature-name
```
- Push with upstream tracking:
  ```bash
git push -u origin feature-name
```
- Force push (use carefully):
  ```bash
git push --force origin feature-name
```

### 3.4 Tracking and Branch Setup
- Set upstream branch:
  ```bash
git branch --set-upstream-to=origin/feature-name
```
- Push tags:
  ```bash
git push origin v1.0.0
```

---

## 4. Advanced Git Commands

### 4.1 Stashing Changes
- Save changes temporarily:
  ```bash
git stash push -m "work in progress"
```
- List stashes:
  ```bash
git stash list
```
- Apply latest stash:
  ```bash
git stash apply
```
- Pop latest stash (apply and remove):
  ```bash
git stash pop
```
- Drop a stash:
  ```bash
git stash drop stash@{0}
```

### 4.2 Rebase
- Rebase current branch onto another:
  ```bash
git rebase main
```
- Interactive rebase to squash/fix commits:
  ```bash
git rebase -i HEAD~3
```
- Abort rebase:
  ```bash
git rebase --abort
```
- Continue after resolving conflicts:
  ```bash
git rebase --continue
```

### 4.3 Cherry Pick
- Apply a commit from another branch:
  ```bash
git cherry-pick <commit-hash>
```
- Abort cherry-pick:
  ```bash
git cherry-pick --abort
```

### 4.4 Reset
- Soft reset (keep changes staged):
  ```bash
git reset --soft HEAD~1
```
- Mixed reset (keep changes unstaged):
  ```bash
git reset HEAD~1
```
- Hard reset (discard changes):
  ```bash
git reset --hard HEAD~1
```

### 4.5 Revert
- Revert a commit without rewriting history:
  ```bash
git revert <commit-hash>
```

### 4.6 Tags
- Create annotated tag:
  ```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
```
- Create lightweight tag:
  ```bash
git tag v1.0.0
```
- Push all tags:
  ```bash
git push origin --tags
```

### 4.7 Bisect
- Start bisect to find a bad commit:
  ```bash
git bisect start
```
- Mark bad commit:
  ```bash
git bisect bad
```
- Mark good commit:
  ```bash
git bisect good <commit-hash>
```

---

## 5. Git Workflow Examples

### 5.1 Feature Branch Workflow
1. Create branch from main:
   ```bash
git checkout main
git pull origin main
git checkout -b feature/xyz
```
2. Work and commit:
   ```bash
git add .
git commit -m "Implement feature xyz"
```
3. Push branch:
   ```bash
git push -u origin feature/xyz
```
4. Create pull request and merge.

### 5.2 Fork and Pull Request Workflow
1. Fork the repo on GitHub.
2. Clone your fork:
   ```bash
git clone https://github.com/yourname/repo.git
```
3. Add upstream remote:
   ```bash
git remote add upstream https://github.com/original/repo.git
```
4. Sync with upstream:
   ```bash
git fetch upstream
git checkout main
git merge upstream/main
```

### 5.3 Commit and Push Safely
- Use small commits with clear messages.
- Use `git status` before adding changes.
- Use `git diff` to review diffs.

---

## 6. Useful Tips and Best Practices

- Always run `git status` before committing.
- Use meaningful commit messages.
- Keep feature branches short-lived.
- Rebase local changes before pushing to avoid merge conflicts.
- Avoid `git push --force` unless necessary.
- Use `.gitignore` for generated files and sensitive data.
- Backup important branches with tags or remote branches.

---

## 7. Git Configurations and Aliases

### 7.1 Common Config
- Set default editor:
  ```bash
git config --global core.editor "code --wait"
```
- Enable color UI:
  ```bash
git config --global color.ui auto
```

### 7.2 Useful Git Aliases
- Add aliases:
  ```bash
git config --global alias.st status
git config --global alias.ci commit
git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.lg "log --oneline --graph --decorate --all"
```
- Use alias:
  ```bash
git lg
```

---

## 8. Git Recovery Commands

### 8.1 Recover Deleted File
- Restore a deleted file from the last commit:
  ```bash
git checkout -- file.txt
```

### 8.2 Undo Last Commit Locally
- Keep changes:
  ```bash
git reset --soft HEAD~1
```
- Discard commit and changes:
  ```bash
git reset --hard HEAD~1
```

### 8.3 Restore Stashed Work
- Apply a stash without removing it:
  ```bash
git stash apply stash@{0}
```
- Restore and delete stash:
  ```bash
git stash pop
```

### 8.4 Recover Lost Commits
- Find dangling commits:
  ```bash
git fsck --lost-found
```

---

## 9. Git and GitHub Notes

- Use GitHub Pull Requests for code review.
- Push branches to remote before opening a PR.
- Keep PR descriptions clear and focused.
- Merge with fast-forward or squash strategy based on project policy.

---

## 10. Summary

This guide is intended to help you move from basic Git usage to advanced workflows. Keep it handy when working on development, fixing issues, or collaborating with teammates.
