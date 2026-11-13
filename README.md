# Zen Browser

An independent Android browser with workspaces, containers and split view.

Zen Browser is a browser that adapts to you. Keep work, study and personal browsing apart in workspaces and
containers, pin the tabs you come back to, and open two pages at once in split view. Your workspaces, containers and
pinned tabs sync with Zen Browser on your computer, or follow Firefox on all your computers and phones. It runs on the
Gecko engine, supports add-ons and tracking protection, and it is private by default.

> **Status:** in active development, and there is no release yet. The rebrand is done and the sidebar is being built;
> themes, the download manager and the rest land slice by slice. Debug builds come from GitHub Actions, see
> [Download](#download).

## Download

No release yet. Every push to `main` builds a debug APK in GitHub Actions:

1. Open the [Actions tab](../../actions) and pick the latest successful run.
2. Download the `dejavu-debug-apk` artifact. You need to be signed in to GitHub — artifacts are not public downloads.
3. Install the APK on your phone. It needs Android 8.0 or newer on a 64-bit phone.

The debug build installs as `com.zen.browser.debug` and runs alongside your other browsers.

## Features

Zen Browser is made of small things that save you time every day.

### Send any tab to your apps

Turn any web service into a button. A custom action sends the page you are on, or every tab you selected, to an app
or server you choose: save it to your read-later app, post it in a chat, send it to your phone as a notification or
start a home automation.

- Write the request once: the method, URL, headers and body, like a `curl` command.
- Fill it in with the page's address, title and site, or its workspace, container, folder and the date, using
  variables such as `${websiteurl}`.
- Start from a ready-made example for Karakeep, Linkwarden, linkding, Readeck, Raindrop.io, Memos, ntfy, Gotify,
  Discord, Slack, Telegram, Home Assistant or any webhook.
- Put the action in the menu, on the buttons of each tab, or on the bar you see when you select several tabs.

### Make the menu yours

- **Edit every button:** Add, remove and move any button in the menu, add new rows, and put your own custom actions
  between them.
- **Choose your actions bar:** Pick the buttons of the actions bar below every page, Home and Search included, so the
  buttons you use most are always one tap away.
- **One list of actions:** Every action, your own included, is in one list. Put each one on pinned tabs, other tabs,
  folders, the bar for selected tabs, the menu or the actions bar. An action on a folder reaches every tab inside it.

### Open links from other apps where they belong

Choose the workspace and container that links from other apps open in: always the same workspace, the one you saw
last, or a private tab. Back takes you straight back to the app you came from.

### Keep everything in its place

- **Workspaces:** Keep work, study and personal browsing apart. Each workspace has its own icon, color theme and tabs,
  and you swipe between them. A private workspace keeps private tabs in one place.
- **Pinned tabs, essentials and folders:** Pin the tabs you come back to, sort them into folders, and keep your
  favorite sites as essentials at the top of the home screen.
- **Containers:** Tabs in a container keep their own cookies and site data, so you can stay signed in to two accounts
  at once. A workspace can open all its tabs in a container. Temporary containers delete their data after their last
  tab is closed.
- **Split view:** Open a link in split view to see two pages at once, or preview a page without leaving the one you
  are reading.

### And more

- **Sync with Zen and Firefox:** Your workspaces, with their colors and icons, containers, pinned tabs, essentials and
  folders stay the same in Zen on your computer and in Zen Browser on your other devices. Unpinned tabs can sync too.
  Or follow Firefox: each of your computers and phones gets its own workspace, with its pinned tabs and tab groups.
- **Private by default:** Telemetry, studies, search suggestions and sponsored content are off until you turn them
  on, and there are no ads.
- **Gecko engine:** Add-ons like uBlock Origin and Dark Reader, Enhanced Tracking Protection, and sync for bookmarks,
  history and passwords through your Mozilla account.

### Set up sync with Zen

1. Sign in to the same Mozilla account in Zen on your computer and in Zen Browser.
2. In Zen, open **Settings**, then **Sync**, and turn on sidebar sync.
3. In Zen Browser, tap the account button at the top left, tap **Sync with** and choose **Zen**. If Zen Browser
   already has workspaces or tabs, choose **Merge** to keep them, or **Start fresh** to replace them with what Zen has.
4. Under **Choose what to sync**, keep **Sync workspaces** on. To sync the tabs that are not pinned as well, turn on
   **Include unpinned tabs** in Zen. It is off in Zen Browser at first, like in Zen, and Zen Browser turns it on once
   it finds tabs that are not pinned in what Zen syncs, unless you turned it off yourself.

### Set up sync with Firefox

1. Sign in to the same Mozilla account in Firefox and in Zen Browser.
2. In Firefox, open **Settings**, then **Sync**, and keep **Open tabs** on.
3. In Zen Browser, tap the account button at the top left, tap **Sync with** and choose **Firefox**. If Zen Browser
   already has workspaces or tabs, choose **Merge** to keep them, or **Start fresh** to see only what Firefox has open.
4. Each computer and phone with Firefox gets its own workspace: pinned tabs stay pinned and tab groups become folders in
   their colors. Open or close a tab there, and Firefox does the same.

## Report a bug or request a feature

Feedback goes through [GitHub issues](../../issues). When you
[open an issue](../../issues/new/choose), pick a template and it starts the title for you:

| Title starts with   | Use it for                                       |
| ------------------- | ------------------------------------------------ |
| `[BUG]`             | Something is broken or does not work as expected |
| `[FEATURE REQUEST]` | An idea or an improvement                        |

Please search the existing issues first. If yours is already there, add a thumbs up reaction to it instead of opening
a new one. Security problems should be reported privately, as [SECURITY.md](SECURITY.md) explains.

Pull requests are welcome too. Start the title with the kind of change, `[FIX]`, `[FEATURE]`, `[DESIGN]`,
`[TRANSLATION]` or `[DOCS]`, and link the issue it solves. [CONTRIBUTING.md](CONTRIBUTING.md) has the details.

## Contributing

Contributions of every kind are welcome, and help with design is especially wanted:

- **Design:** screens, flows and small details that make Zen Browser simpler and easier to use.
- **Icon packs:** new icons, alternative icon packs and app icons.
- **Themes:** new color themes for workspaces.
- **Translations:** corrections for the app's own texts in your language.
- **Code:** bug fixes and new features.
- **Testing:** try Zen Browser on your phone or tablet and tell us what breaks.

[CONTRIBUTING.md](CONTRIBUTING.md) explains how each of these works and how to build Zen Browser.

## Build from source

This repository is a fork of [Dejavu](https://github.com/by-architect/Dejavu), which is itself a fork of Mozilla's
Firefox repository. Its engine does not follow Firefox's releases yet: it is a Firefox 159 Nightly build from
September 25, 2026, so fixes Mozilla makes in Firefox after that do not reach this project until it moves to a newer
Firefox. Moving to Firefox's releases, and then following each one, is planned. The Android app is in
`mobile/android/fenix`; this project's own code lives in the `org.mozilla.fenix.sidebar` package and Dejavu's own code
in `org.mozilla.fenix.dejavu`. The steps to build it and run it on a phone are in
[CONTRIBUTING.md](CONTRIBUTING.md#build-dejavu).

Every push to `main` also builds the debug APK in GitHub Actions — see
[.github/workflows/android-debug.yml](.github/workflows/android-debug.yml).

## Privacy

Zen Browser does not collect your data. The [privacy policy](PRIVACY.md) explains what stays on your device and what
the optional features, like sync, share with Mozilla and your search engine.

## License

Zen Browser is licensed under the [Mozilla Public License 2.0](LICENSE), like Firefox. Some third-party parts of the
code have their own licenses, listed in [toolkit/content/license.html](toolkit/content/license.html).

Zen Browser is an independent project, not made or endorsed by Mozilla or the Zen Browser team. It is built on
[Dejavu](https://github.com/by-architect/Dejavu) by The Architect and on Firefox by Mozilla. Firefox is a trademark
of the Mozilla Foundation, and this repository grants no rights to Mozilla's trademarks.
