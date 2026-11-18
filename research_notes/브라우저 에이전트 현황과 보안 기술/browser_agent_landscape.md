# Browser agent product landscape (state as of 2026-11-18)

Verification legend used on every finding:
- **[opened]** = I fetched and read the cited page in this session (via a summarising fetch tool, so wording is paraphrased unless quoted).
- **[snippet]** = taken from a search-result summary only; the page itself was NOT opened. Treat as lower confidence.
- **[memory]** = 2024-2025 historical fact from the researcher's background knowledge; the URL is the canonical announcement but was NOT opened in this session.

General caveat: most 2026 facts below come from secondary press/blogs relaying vendor posts. Primary vendor pages I actually opened are only: Anthropic's two 2026-08-26 posts and Microsoft's 2026-05-13 Edge blog. OpenAI and Google primary pages were not opened (help.openai.com / blog.google not fetched), so OpenAI/Google statements are second-hand.

---

## Q1. Which browser agents exist today, and what is each one's status (launch/retirement, plans, countries, platforms)?

### Takeaway
Between mid-2025 and Nov 2026 the field consolidated: the three "first wave" standalone products (OpenAI Operator, Google Project Mariner, ChatGPT Atlas) were all retired and folded into general-purpose agents (ChatGPT Work, Gemini Spark/Tasks, Chrome auto browse), while Perplexity Comet, Claude in Chrome (GA Aug 2026) and Chrome's built-in auto browse are the main live consumer browser agents. Browser control is now mostly one tool inside a broader "work agent" rather than a product of its own.

### Cited Findings

#### OpenAI
- (Historical) Operator launched 2025-01-23 as a research preview for ChatGPT Pro users in the US, running the Computer-Using Agent (CUA) model in an OpenAI-hosted remote browser. [memory] — [OpenAI](https://openai.com/index/introducing-operator/)
- (Historical) Operator was folded into ChatGPT agent in mid-2025; the separate Operator site was deprecated 2025-08-01 and access ended 2025-08-31. [snippet] — [usecarly.com summary](https://www.usecarly.com/blog/what-is-chatgpt-agent/)
- (Historical) ChatGPT agent launched 2025-07-17 (virtual computer with visual browser, text browser, terminal). [memory] — [OpenAI](https://openai.com/index/introducing-chatgpt-agent/)
- ChatGPT agent (agent mode) is retired. OpenAI's help page reportedly states "ChatGPT agent is no longer available" (third-party check dated 2026-10-26); that article gives no retirement date. [opened] — [omidsaffari.com, 2026-10](https://omidsaffari.com/blog/chatgpt-agent-retired). Another guide says it was removed "in early August 2026, with no advance deprecation notice". [snippet] — [search summary of same/related guides](https://www.usecarly.com/blog/what-is-chatgpt-agent/). **Conflict/uncertainty: exact retirement date not established from a primary source.**
- (Historical) ChatGPT Atlas, a Chromium-based browser with ChatGPT sidebar and agent mode, launched 2025-10-21. [opened] — [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/); [tidbits, 2026-07-13](https://tidbits.com/2026/07/13/openai-shuffles-chatgpt-apps-kills-atlas-browser-improves-voice/)
- Atlas retirement was announced 2026-07-09 ("We'll begin sunsetting the standalone Atlas browser") together with ChatGPT Work. [opened] — [tidbits](https://tidbits.com/2026/07/13/openai-shuffles-chatgpt-apps-kills-atlas-browser-improves-voice/); [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/)
- Atlas was "scheduled to stop working on August 9, 2026" per OpenAI's help article "Evolving Atlas into ChatGPT for browser-based agentic work"; bookmarks/tabs/history/sessions do not migrate automatically; browser-agent capabilities move into ChatGPT and Codex. [opened, secondary relay of the OpenAI help article] — [letsdatascience.com](https://letsdatascience.com/news/openai-shutters-standalone-chatgpt-atlas-desktop-browser-0cc642c2). Atlas lived 292 days. [opened] — [No Hacks landscape guide, updated 2026-10-31](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- ChatGPT Work (launched 2026-07-09) is the current OpenAI agent: multi-step office tasks running "for hours", scheduled tasks, plugins (Slack, Teams, Drive, SharePoint, etc.), a built-in browser in the unified desktop app (Mac and Windows), desktop Computer Use (click/type/move files on the user's machine in the background), and an updated Chrome extension running ChatGPT in Chrome's sidebar. Web/mobile rollout: Pro, Enterprise, Edu on 2026-07-09, Plus and Business within days; desktop app modes on every tier including free. [opened] — [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/)
- Inside Work, a "cloud browser" handles website steps, keeps working after the user leaves, has its own sessions ("A browser login on your laptop does not sign in the cloud browser"), pauses for sign-in/input/confirmation; available on paid plans in supported regions (not Free/Go). [opened] — [omidsaffari.com](https://omidsaffari.com/blog/chatgpt-agent-retired)
- From 2026-10-15 the ChatGPT Chrome extension can take over tabs in Work mode; the extension had ~2 million users when Atlas was announced. [opened, tertiary] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- The new desktop app merges Chat, ChatGPT Work and Codex; it is Electron, ~1.45 GB. [opened] — [tidbits](https://tidbits.com/2026/07/13/openai-shuffles-chatgpt-apps-kills-atlas-browser-improves-voice/)

#### Google
- (Historical) Project Mariner was announced Dec 2024 as a Chrome-extension research prototype, then moved to cloud VMs for AI Ultra subscribers in May 2025. [memory] — [TechCrunch 2025-05-20](https://techcrunch.com/2025/05/20/google-rolls-out-project-mariner-its-web-browsing-ai-agent/)
- Project Mariner was shut down in early May 2026 (search summaries say 2026-05-04; Android Authority's 2026-05-06 article gives no exact date). Google's landing page said the technology "will live on in other products"; staff were moved off the team in March 2026 (per Wired, cited by AA); features go to the Gemini API and Gemini Agent. Mariner worked by frequent screenshots + clicking/typing in Chrome and was described as resource-intensive, slow and error-prone. [opened] — [Android Authority 2026-05-06](https://www.androidauthority.com/google-project-mariner-shutdown-3664323/)
- Chrome "auto browse" (Gemini 3) launched 2026-01-28 on desktop for Google AI Pro and AI Ultra subscribers in the US; it scrolls, clicks, types and navigates. [opened, tertiary] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026); corroborated as "preview on desktop in January" [opened] — [Android Authority 2026-05-12](https://www.androidauthority.com/chrome-auto-browse-on-android-3665290/). The No Hacks page is internally inconsistent (timeline says "all users", body says Pro/Ultra only).
- Auto browse is not available for managed (enterprise) Chrome users per Google's admin help page. [snippet] — [Google Chrome Enterprise help](https://support.google.com/chrome/a/answer/16291696)
- Auto browse on Android: announced 2026-05-12, rollout from end of June 2026; AI Pro/Ultra, US only, Android 12+, 4 GB+ RAM, English (US); user must take over for purchases and for Google Password Manager credentials. [opened] — [Android Authority 2026-05-12](https://www.androidauthority.com/chrome-auto-browse-on-android-3665290/). Gemini in Chrome reached all US Android users on 2026-08-18 while auto browse stayed paid/US-only; not available in Gemini in Chrome on iPhone/iPad. [snippet] — [unite.ai](https://www.unite.ai/gemini-in-chrome-opens-to-all-u-s-android-users-as-auto-browse-goes-mobile/); [Google blog](https://blog.google/products-and-platforms/products/chrome/bringing-chrome-ai-to-android/)
- Gemini Spark (personal background agent) debuted May 2026 in the US for AI Ultra, reached US AI Pro about a week before 2026-07-31, and expanded to paid subscribers in "over 160 new countries" on 2026-07-30; on 2026-07-30 Google also announced that, with permission, Spark can use Chrome logged-in accounts and saved passwords ("auto browse" in Chrome, US). Payments and sensitive actions require human validation. [opened] — [Thurrott 2026-07-31](https://www.thurrott.com/a-i/339900/googles-gemini-spark-agent-expands-to-more-countries-and-adds-new-chrome-features). Spark was not available in the EEA, UK, Switzerland, Nigeria. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)
- 2026-10-13: Gemini app gained a Chat/Task toggle (Gemini 4 Argon) for AI Pro and Ultra; 2026-11-17: Google's Gemini blog said task mode is free for "hundreds of millions of people" in the US, Japan, South Korea, Canada, Australia and India, running on Gemini 4 Hydrogen (introduced 2026-11-12). Calls to businesses, Google Pay checkout (AP2 with Mastercard/Visa), texting and email forwarding are US-first. Connected apps: 60+. Usage limits for free tasks are unstated in Google's post. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)
- The Verge (2026-11-17), as relayed by DigitalToday, describes this as Gemini Spark being renamed "Tasks" and offered free in the six countries. [opened] — [DigitalToday 2026-11-18](https://www.digitaltoday.co.kr/en/view/127105/google-revamps-ai-agent-gemini-spark-into-tasks-adds-support-in-south-korea). One blog claims free accounts have a compute budget resetting every five hours plus a weekly cap. [snippet, unverified] — [search summary](https://www.fdaytalk.com/free-gemini-ai-agent/). **Conflict: one search summary said ppc.land found Google's post silent on free users; the ppc.land page I opened says the opposite (Google's post says free for "hundreds of millions"). I rely on the opened page.**
- Plan prices (Google docs, 2026-10-04): AI Plus $4.99/mo, AI Pro $19.99, AI Ultra $99.99 or $199.99. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)
- A separate enterprise "Gemini agent" was unveiled at Google Cloud "Gemini at Work 2026" (2026-10-08), in private preview with no price. [snippet] — [wikidocs](https://wikidocs.net/blog/@openwiki/32950/)
- Gemini computer-use models: Gemini 2.5 Computer Use model announced 2025-10-07 (browser-focused, `computer_use` tool in Gemini API) [memory] — [Google](https://blog.google/innovation-and-ai/models-and-research/google-deepmind/gemini-computer-use-model/); Computer Use came to Gemini 3 Pro/Flash previews in Jan 2026; in June 2026 it became a built-in tool of Gemini 3.5 Flash covering browser, mobile and desktop environments, still labelled Preview in docs. [snippet] — [Android Authority](https://www.androidauthority.com/gemini-3-5-flash-computer-use-model-3681381/); [Neowin](https://www.neowin.net/news/google-adds-built-in-computer-control-to-gemini-35-flash/). A third-party aggregator claims GA; unconfirmed.

#### Anthropic
- (Historical) Computer use tool launched in public beta Oct 2024 (Claude 3.5 Sonnet); screenshot-in, mouse/keyboard-out, developer-hosted environment. [memory] — [Anthropic](https://www.anthropic.com/news/3-5-models-and-computer-use)
- (Historical) Claude for Chrome: limited preview for Max subscribers Aug 2025; expanded to Pro, Team, Enterprise Dec 2025; Anthropic reported prompt-injection success falling 23.6% -> 11.2% at pilot time. [opened, tertiary] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- 2026-08-12: the Claude in Chrome side panel became a full Cowork client (sessions, skills, connectors shared with desktop/web/mobile); Max and Team first, Pro later. [snippet] — [The New Stack](https://thenewstack.io/claude-chrome-cowork-sessions/)
- 2026-08-26: Claude in Chrome became "generally available on every paid Claude plan". It uses the user's existing logins, works across tabs, auto-approves safe actions (same mechanism as Claude Code auto mode; can be turned off), with a safety classifier checking each action against the original request. Chrome desktop only: "does not run on other Chromium browsers or on mobile yet". Enterprise admins can restrict to approved domains. [opened, primary] — [Anthropic 2026-08-26](https://claude.com/blog/claude-in-chrome-generally-available)
- Same post, prompt-injection numbers (brief; security covered elsewhere): against stronger red-team attacks before added safeguards, 17.6% success on Opus 4.5 vs 3.8% on Opus 5; with probes + classifier from Opus 4.8 onward, no successful attacks on Sonnet 5 / Opus 5 / Mythos 5 and 0.3% on Fable 5; Anthropic says injection "remains a moving target". [opened, primary] — [Anthropic](https://claude.com/blog/claude-in-chrome-generally-available)
- 2026-08-26: Cowork got a built-in browser (side panel of the Claude desktop app on macOS, Windows, Linux beta) for Pro, Max, Ultra, Team; Enterprise admin-enabled. It is separate from the user's browser ("Claude never sees your tabs, bookmarks, or passwords"); logins can be imported site-by-site from Chrome/Edge/Firefox, with banking, email and SSO sites excluded by default. If Claude in Chrome is installed it stays the default browser for Cowork. [opened, primary] — [Anthropic 2026-08-26](https://claude.com/resources/articles/cowork-built-in-browser)

#### Microsoft
- (Historical) Edge Copilot Mode launched July 2025; Copilot Actions and Journeys previewed Oct 2025 (US, limited preview). [snippet] — [Windows Central](https://www.windowscentral.com/microsoft/windows-11/microsoft-edge-copilot-actions-journeys-available-2025)
- 2026-05-13: "we're retiring Copilot Mode"; features are built into regular Edge. Copilot Actions became "Browse with Copilot on Edge desktop for Microsoft 365 Premium Subscribers in the U.S. only", usage limits apply. Vision, Voice, multi-tab context and long-term memory are in all Copilot markets on desktop and mobile; Journeys desktop is English markets only, mobile US only. [opened, primary] — [Microsoft Edge blog 2026-05-13](https://blogs.windows.com/msedgedev/2026/05/13/new-updates-to-edge-across-desktop-and-mobile/)
- Edge 150 (July 2026) opened a WebMCP origin trial through 2027-03-30. [opened, tertiary] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)

#### Perplexity Comet
- Comet: desktop July 2025 (initially Max-only), free for everyone Oct 2025, Android 2025-11-20, iOS 2026-03-18 (slipped from 03-11); mobile apps are free and include the assistant with some agentic capability; Pro $20/mo and Max $200/mo raise limits. [snippet] — [Thurrott](https://www.thurrott.com/mobile/333936/perplexity-launches-its-comet-browser-on-ios-and-ipados); [TechCrunch 2025-10-02](https://www.techcrunch.com/2025/10/02/perplexitys-comet-ai-browser-now-free-max-users-get-new-background-assistant/)
- March 2026: Comet enterprise launch with silent MDM deployment on macOS/Windows. Amazon v. Perplexity: preliminary injunction 2026-03-10, stayed on appeal, vacated by the Ninth Circuit 2026-08-04. [opened, tertiary] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)

#### Other browsers
- Dia / Arc (The Browser Company): Atlassian acquisition announced 2025-09-04 at ~$610M, closed 2025-10-20 (filings: ~$488M cash plus vesting stock). Dia is GA for macOS 14+ Apple Silicon, paid tier with 14-day trial; Arc "receives Chromium updates only". [opened, tertiary] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026). **Conflict on Windows: No Hacks (updated 2026-10-31) says Windows is beta-waitlist; another review says Dia launched on Windows 2026-10-07 without the full agentic experience** [snippet] — [wetheflywheel](https://wetheflywheel.com/en/radar/dia-browser-review/). Reviewers classify Dia as "not fully" agentic.
- Opera Neon: announced Sept 2025, public Dec 2025 at $19.90/mo; Feb 2026 Intelligent Mode; free plan added 2026-08-14; agents Neon Do / Neon Make / ODRA; can be exposed as an MCP server. [opened, tertiary] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- Brave: "AI browsing" (Leo agentic mode) entered early testing in Nightly 2025-12-10 behind a flag, in an isolated profile whose cookies/logged-in state are separate from the regular profile; Brave's post was updated 2026-05-05 saying it is in early testing on all release channels. [snippet] — [Brave blog](https://brave.com/blog/ai-browsing/). **Conflict: No Hacks lists Brave's agentic capability as "None"** [opened] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026).
- Mozilla Firefox: AI kill switch in Firefox 148 (2026-02-24); "Smart Window" beta 2026-08-18; Firefox 155 (2026-09-01) opened it to all users in the US, Canada, France. It is web-aware chat + tab management, not a click-and-type agent. [opened, tertiary] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- Samsung: "Samsung Browser" for Windows launched late March 2026 with Perplexity-powered "Browsing Assist" (also on Galaxy Android); agentic features reportedly limited to South Korea and the US. [snippet + opened tertiary] — [Samsung Mobile Press](https://samsungmobilepress.com/articles/samsung-browser-windows-agentic-ai); [Perplexity blog](https://hub-prod.perplexity.ai/hub/blog/perplexity-apis-power-ai-browsing-on-samsung-devices); [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- Genspark browser (May 2025; Autopilot Mode, Super Agent, MCP store) and Fellou (full agent with editable workflow plans, uses logged-in sessions) exist as standalone agentic browsers. [opened, tertiary] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- China: Alibaba Quark, ByteDance Doubao and Meituan's "Tabbit" are all marketed as AI agents rather than browsers; a June 2026 hands-on rated Tabbit strongest at form-fill/price-compare/data-export; Doubao professional edition added an "office task mode" that, once authorised, operates the PC's apps, browser and files. [snippet] — [36Kr](https://www.36kr.com/p/3855958190127752)
- Manus (Butterfly Effect): cloud virtual computer + browser. Meta agreed to buy it in late Dec 2025 (reported >$2B); Caixin (2026-08-12) reports the deal unwound and Manus will resume independent operation. [snippet] — [Caixin](https://www.caixinglobal.com/2026-08-12/tech-brief-aug-12-manus-to-resume-independent-operations-as-meta-deal-unwinds-102473330.html); [SiliconANGLE 2025-12-29](https://siliconangle.com/2025/12/29/meta-platforms-buys-manus-bolster-agentic-ai-skillset/). **Conflict: early-2026 sources describe the deal as completed.**

#### Developer / infrastructure layer (brief)
- Amazon Nova Act reached GA 2025-12-02 as an AWS service for fleets of UI-workflow agents (US East), hourly pricing. [snippet] — [AWS](https://aws.amazon.com/blogs/aws/build-reliable-ai-agents-for-ui-workflow-automation-with-amazon-nova-act-now-generally-available)
- Browserbase: ~$67.5M total funding (Series B); Stagehand v4.0.0 on 2026-08-10 (MIT). Playwright MCP (Microsoft) v0.0.79 on 2026-08-06 (Apache-2.0). Chrome DevTools MCP exposes CDP (network, console, script evaluation). Browser Use: ~$17M seed (2025). [snippet, third-party aggregators] — [Contrary Research](https://research.contrary.com/company/browserbase); [nikiforovall blog](https://nikiforovall.blog/ai/2026/07/05/browser-automation-agents-ecosystem.html)
- WebMCP (sites exposing tools to in-browser agents) is in origin trial in Edge 150 and experimental in Brave. [opened, tertiary] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)

### Inferences
- "Browser agent as a standalone product" largely failed commercially at the model vendors (Operator ~7 months, Mariner ~17 months, Atlas ~10 months), but the capability persists everywhere as a tool inside a general agent. The report should describe the Nov 2026 state as "browser control as a feature" not "agentic browser as a category winner".
- As of Nov 2026 the widest-reach browser-capable agents are probably Gemini Tasks (free, six countries) and ChatGPT Work's cloud browser (paid), neither of which is a browser product.
- Vendor naming churn is high (Spark -> Tasks; Copilot Actions -> Browse with Copilot; Claude for Chrome -> Claude in Chrome; agent mode -> Work), so the report should give both names.

### Gaps
- No primary OpenAI page opened; exact ChatGPT agent retirement date unknown; whether Atlas actually stopped on 2026-08-09 relies on HUMAN/No Hacks secondary statements.
- Could not confirm from Google's own post that "Tasks" is an official rename of Spark, or free-tier limits.
- Whether Chrome auto browse is available outside the US (incl. Korea) on desktop as of Nov 2026: no source found stating expansion; all sources say US-only.
- Copilot Studio computer use, Windows agent workspace / Copilot Actions on Windows: no 2026 source found in this session.
- Arc Search, Genspark/Fellou current status, Quark specifics, Kakao: no reliable 2026 data found.
- Brave AI browsing stable-release status after May 2026 unknown.

---

## Q2. Architecture classes: where does the browser run, whose sessions, what does the model observe?

### Takeaway
Four classes coexist and several vendors now ship two at once (a session-sharing mode and an isolated mode), which is the key security-relevant product fact of 2026: Anthropic (Claude in Chrome vs Cowork built-in browser), OpenAI (Chrome extension vs cloud browser) and Google (Chrome auto browse vs per-user cloud VM for Tasks) each let the agent run either inside the user's logged-in browser or in a separate environment with its own sessions.

### Cited Findings

| Class | Products (Nov 2026) | Browser location | Sessions used | Evidence |
|---|---|---|---|---|
| (a) Extension / built-in agent in the user's own mainstream browser | Claude in Chrome; ChatGPT Chrome extension (Work mode tab takeover from 2026-10-15); Chrome auto browse (built into Chrome desktop and Android); Edge Browse with Copilot | User's device, user's profile | User's real logged-in sessions | below |
| (b) AI-native browser with built-in agent | Perplexity Comet; Opera Neon; Dia (partially agentic); Genspark; Fellou; Samsung Browser Browsing Assist; (retired) ChatGPT Atlas; Brave AI browsing (isolated profile variant) | User's device | User's sessions in that browser (Brave: separate profile) | below |
| (c) Vendor-operated remote/cloud browser or VM | ChatGPT Work cloud browser; Gemini Tasks/Spark (per-user cloud VM); Manus; (retired) Operator, ChatGPT agent, Project Mariner (2025 version) | Vendor cloud | Agent's own sessions; user must log in inside it or hand over credentials | below |
| (c') Isolated local browser driven by a desktop agent | Claude Cowork built-in browser; ChatGPT desktop app built-in browser | User's device, separate from user's browser | Separate; optional per-site login import | below |
| (d) Model API + developer-run browser | Anthropic computer use tool; Gemini computer_use tool (3.5 Flash); OpenAI CUA/computer-use API; Nova Act; Browser Use; Stagehand/Browserbase; Playwright MCP; Chrome DevTools MCP | Developer infra or cloud browser service | Whatever the developer provisions | below |

- Claude in Chrome gives Claude access "using your existing logins"; it can view the current page, read/type text, click, navigate, fill forms; Anthropic's post does not specify screenshots vs DOM, saying page content arrives via tool results. [opened, primary] — [Anthropic](https://claude.com/blog/claude-in-chrome-generally-available)
- Cowork built-in browser: "Claude never sees your tabs, bookmarks, or passwords"; logins brought over site by site; banking/email/SSO excluded by default; runs in the desktop app and can be driven from web/phone while the desktop app is online. [opened, primary] — [Anthropic](https://claude.com/resources/articles/cowork-built-in-browser)
- ChatGPT Work cloud browser keeps its own sessions and does not inherit laptop logins; continues after the user leaves. [opened] — [omidsaffari.com](https://omidsaffari.com/blog/chatgpt-agent-retired). ChatGPT desktop app also has a built-in browser and separate OS-level Computer Use. [opened] — [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/)
- Gemini Tasks: each user's tasks run in a hardware-isolated, dedicated cloud VM on Google Cloud "Agent Compute Engine"; in the US it can use Google Password Manager credentials to log in to sites and pay with Google Pay; pauses before critical steps. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/); [DigitalToday](https://www.digitaltoday.co.kr/en/view/127105/google-revamps-ai-agent-gemini-spark-into-tasks-adds-support-in-south-korea)
- Chrome auto browse (desktop, US) can use saved logins/passwords but hands payment steps back to the user; with permission Spark can use Chrome's logged-in accounts. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/); [Thurrott](https://www.thurrott.com/a-i/339900/googles-gemini-spark-agent-expands-to-more-countries-and-adds-new-chrome-features)
- Project Mariner observed via frequent screenshots and acted by click/type in Chrome. [opened] — [Android Authority](https://www.androidauthority.com/google-project-mariner-shutdown-3664323/)
- Brave AI browsing uses an isolated profile; cookies, logged-in state and caches do not cross profiles. [snippet] — [Brave](https://brave.com/blog/ai-browsing/)
- Samsung Browsing Assist calls Perplexity's Agent/Search API on a dedicated single-tenant Perplexity cluster with claimed zero data retention (vendor claim). [snippet] — [Perplexity](https://hub-prod.perplexity.ai/hub/blog/perplexity-apis-power-ai-browsing-on-samsung-devices)
- Chrome DevTools MCP gives agents protocol-level access (network requests, console, script evaluation), i.e. more than a screenshot view. [snippet] — [Browserbase blog](https://browserbase.com/blog/best-mcp-servers-for-browser-automation)
- Android "task automation" (2026-11-10, extended to UK, India, Japan) runs in an on-device virtual phone, distinct from the cloud VM used for Tasks. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)

### Inferences
- Observation modality by class (general pattern; only partly sourced above): API computer-use tools (Anthropic, Gemini, CUA) are screenshot-based; extensions/AI-native browsers combine page text/DOM/accessibility tree with screenshots; developer frameworks (Playwright MCP, Stagehand, Browser Use) lean on accessibility-tree/DOM snapshots. I did not open sources this session confirming the modality for Comet, Atlas, auto browse or Neon, so the report should phrase this cautiously.
- The security trade-off is now exposed as a user-facing setting (e.g. Cowork "Preferred browser"; Chat vs extension vs cloud browser in ChatGPT): isolated environments limit blast radius but need credential hand-off; in-profile agents inherit every logged-in session.
- Vendors exclude high-risk sites from session sharing by default (Anthropic: banking/email/SSO) and gate payments/credentials on human confirmation (Google, OpenAI).

### Gaps
- No vendor documentation opened that states exactly what Comet, Atlas, Chrome auto browse or Edge Browse with Copilot feed to the model (screenshots vs DOM vs accessibility tree).
- Whether ChatGPT desktop built-in browser shares OS-browser cookies: not found.

---

## Q3. Capability status: benchmarks, reliability, what users do

### Takeaway
On the classic benchmarks, 2026 systems are at or past saturation and above the cited human baseline (WebVoyager ~99%, Online-Mind2Web 90-97%, OSWorld-Verified ~85% vs ~72% human), but harder refreshed benchmarks (OSWorld 2.0 ~71-78%, WebArena ~74%, ClawBench 33%, HealthAdminBench 52%) and press reviews still show frequent errors; real-world agent traffic is concentrated on search/product browsing, with only ~2% reaching checkout.

### Cited Findings
- Steel.dev aggregated leaderboard (last updated 2026-10-11), top entries: WebVoyager — browser-control (Fable 5) 99.19%, Alumnium 98.5%, Surfer 2 97.1%; Online-Mind2Web — Browser Use Cloud (bu-max) 97.0%, GPT-5.4 Native Computer Use 93.0%, ABP + Claude Opus 4.6 90.53%; WebArena — WebTactix (DeepSeek v3.2) 74.3%, OpAgent 71.6%, ColorBrowserAgent 71.2%; OSWorld — Qwen3.8-Max 86.1%, Claude Mythos Preview 85.4%, Claude Mythos 5 85.0%; OSWorld 2.0 — Claude Fable 5.1 77.9%, Simular Sai 73.0%, GPT-6 Astra 72.6%; BrowseComp — GPT-5.6 Sol Ultra 92.2%, GPT-6 Astra 91.5%, Kimi K3 91.2%; ClawBench — Claude Sonnet 4.6 33.3%; HealthAdminBench — Claude Mythos 5 (browser-use) 51.9%. [opened; aggregator, many scores self-reported] — [leaderboard.steel.dev](https://leaderboard.steel.dev/)
- Other trackers disagree on the OSWorld-Verified leader: Yutori (updated 2026-08-07) lists "Intelligence-Indeed Agent" 90.2%; BenchLM (2026-10-15) lists Qwen3.8 Max 86.1%, Claude Fable 5 85%, Claude Mythos 5 85%. Human baseline usually cited ~72% (72.36%). [snippet] — [Yutori](https://yutori.com/leaderboards); [BenchLM](https://benchlm.ai/benchmarks/osWorldVerified)
- "Computer-use agents hit 85% on OSWorld, up from 12% in two years." [snippet, headline] — [cryptobriefing](https://cryptobriefing.com/computer-use-agents-85-percent-osworld-benchmark/)
- OSWorld 2.0 is a harder separate protocol; Claude Opus 5 (released 2026-07-24) reported at ~70.6% (BenchLM, 2026-08-22). [snippet] — [BenchLM](https://www.benchlm.ai/benchmarks/osworld2)
- Online-Mind2Web = 300 tasks over 136 live sites. [snippet] — [Yutori](https://yutori.com/leaderboards)
- Reliability in reviews: Gemini Spark praised for Google-service integration but criticised for errors requiring user intervention (The Verge) and an omitted attendee / unfinished external booking (Wired), as relayed by Korean press. [opened] — [Daum/유미's 픽 2026-08-01](https://v.daum.net/v/20260801074138008). Google itself says of Tasks mistakes "we haven't eliminated all of them". [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)
- Speed: Claude in Chrome "Quick Mode" (March 2026) described as 3x faster; ChatGPT Work tasks can run for hours unattended. [opened, tertiary] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026); [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/)
- What agents do in the wild (HUMAN Security network telemetry): May 2026 — product and search routes 76.4% of agentic traffic, authentication 5.6%, checkout/payment 2.4%; ecommerce, media, travel dominate; July 2026 media 43.5% overtook e-commerce 42.0%; annual report: agentic traffic +7,851% YoY and 2.3% of agentic activity at checkout. [snippet] — [HUMAN May 2026](https://www.humansecurity.com/learn/blog/state-of-agentic-traffic-may-2026-financial-services-agentic-traffic-continues-to-climb-more-than-doubling-this-month/); [HUMAN July 2026](https://www.humansecurity.com/learn/blog/state-of-agentic-traffic-july-2026-publishers-claim-highest-share-of-agentic-traffic/); [HUMAN 2026 benchmark report](https://www.humansecurity.com/2026-state-of-ai-traffic-cyberthreat-benchmark-report/)
- Vendor-suggested use cases: internal dashboards/legacy systems/vendor portals with no connector, form filling, drafting email replies (Anthropic) [opened] — [Anthropic](https://claude.com/blog/claude-in-chrome-generally-available); invoice collection and research in background (Cowork browser) [opened] — [Anthropic](https://claude.com/resources/articles/cowork-built-in-browser); apartment viewings, flight research, parking search (Google) [opened] — [Thurrott](https://www.thurrott.com/a-i/339900/googles-gemini-spark-agent-expands-to-more-countries-and-adds-new-chrome-features); [Android Authority](https://www.androidauthority.com/chrome-auto-browse-on-android-3665290/)

### Inferences
- Benchmark numbers are mostly vendor/self-reported and aggregator-collected with differing harnesses; top WebVoyager/Online-Mind2Web entries are agent scaffolds rather than raw models. The report should present them as "reported" and avoid cross-benchmark comparisons.
- The gap between ~99% WebVoyager and ~2% of real agent traffic reaching checkout suggests real usage is still dominated by read/compare tasks, with transactions gated by human confirmation.

### Gaps
- Official leaderboards (os-world.github.io, Online-Mind2Web HAL) and model system cards were not opened; no vendor system card figure verified.
- No reliable published data on typical task duration or success rates for consumer products (Comet, auto browse, Work) found.
- Model names on leaderboards (GPT-6 Astra, Gemini 4, Qwen3.8, Fable 5.1, etc.) post-date researcher's background knowledge and are reported as shown by the aggregator.

---

## Q4. Adoption and market facts

### Takeaway
Agentic browsers remain a rounding error in browser market share (none in StatCounter's top six; Chrome ~68%), and no vendor publishes audited user counts; the best observable proxy is HUMAN Security's agent-traffic share, where Comet holds ~40-48% all year, Claude's extension rose to ~22-24%, and Atlas fell from ~21% to shutdown.

### Cited Findings
- StatCounter-based snapshot July 2026: no AI-native browser in the global top six; Chrome ~68%, Firefox 3.3%. [snippet] — [dupple.com](https://dupple.com/learn/best-ai-browser)
- HUMAN Security monthly share of observed agentic traffic: April 2026 — Comet 48.12%, Atlas 21.33%, Claude Chrome extension 17.33%, ChatGPT Agent 8.55%; May — Comet 47%, Atlas 20.3%; June — Comet 47.6%, Claude 20.8%, Atlas 16.5%; July — Comet 47.1%, Claude 24%, Atlas 15.5%; August — Comet 40.7%, Claude 22%, Codex debut 7.3%, overall agentic traffic +27% to a new high. [snippet; August page returned HTTP 403 when I tried to open it] — [HUMAN April](https://humansecurity.com/learn/blog/state-of-agentic-traffic-april-26); [HUMAN June](https://www.humansecurity.com/learn/blog/state-of-agentic-traffic-june-2026-browser-agent-tooling-for-developers-is-catching-on-fast/); [HUMAN August](https://www.humansecurity.com/learn/blog/state-of-agentic-traffic-august-2026-agentic-traffic-grows-27-reaches-new-high-as-codex-debuts-strongly/); May figures also [opened] via [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/). Caveat: only HUMAN-protected sites; identification partly by user-agent.
- Comet user numbers conflict: ~18M MAU in Q1 2026 (third-party tracker), 3M MAU (Similarweb-based), "10M users by early 2026" (review site); Perplexity has not published a verified figure; >1M mobile downloads by early 2026. [snippet] — [presenc.ai](https://presenc.ai/research/comet-usage-statistics-2026); [aitoolanalysis](https://aitoolanalysis.com/comet-browser-review/)
- OpenAI did not share Atlas usage or cost figures. [opened] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- Scale context: ChatGPT ~1.1B, Gemini ~900M, Claude ~245M active users (as cited by TidBITS, July 2026); Codex >5M weekly users incl. >1M outside software development. [opened] — [tidbits](https://tidbits.com/2026/07/13/openai-shuffles-chatgpt-apps-kills-atlas-browser-improves-voice/); [ppc.land](https://ppc.land/openai-kills-atlas-browser-folds-it-into-new-chatgpt-work-agent/)
- Google frames free Tasks as reaching "hundreds of millions of people"; Chrome user base ~3B; Android auto browse target 200M devices by end-2026. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/); [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- Pricing snapshot: Comet free (Pro $20, Max $200); Opera Neon $19.90/mo plus free plan since 2026-08-14; Claude in Chrome on all paid Claude plans; Chrome auto browse requires AI Pro ($19.99) or Ultra; Gemini Tasks free in six countries; Browse with Copilot requires Microsoft 365 Premium (US); ChatGPT Work cloud browser on paid plans (not Free/Go), Codex-style usage credits; Brave Leo Premium $14.99. [opened/snippet as cited in Q1]
- Shutdowns/pivots: Operator (Aug 2025), Project Mariner (May 2026), Copilot Mode brand (May 2026), ChatGPT Atlas (Aug 2026), ChatGPT agent mode (by Aug-Oct 2026), Arc (maintenance only), Dia sold to Atlassian, Manus-Meta deal unwound (Aug 2026). [as cited in Q1]
- Android Authority's framing of Mariner's end: coding/file agents such as Claude Code and OpenClaw became more popular than click-through-the-web agents. [opened] — [Android Authority](https://www.androidauthority.com/google-project-mariner-shutdown-3664323/)
- Enterprise: Comet enterprise with MDM (Mar 2026); Claude in Chrome admin domain allowlists; Chrome auto browse not offered to managed users; Samsung Browsing Assist claims reach of >1B devices (Perplexity claim). [as cited above]

### Inferences
- The market moved from "new browser" bets to distribution through incumbents (Chrome, Edge, Samsung) and assistant apps; standalone AI browsers other than Comet have not shown measurable share.
- Free distribution is the 2026 trend (Comet Oct 2025, Neon Aug 2026, Gemini Tasks Nov 2026), implying rapid growth in the exposed population irrespective of security maturity.

### Gaps
- No audited user numbers for any product; no notable named enterprise deployments found.
- HUMAN data not opened directly (403), numbers from search summaries.

---

## Q5. Availability in South Korea and Korean products

### Takeaway
Korea got assistant-level features first (Gemini in Chrome, April 2026) and agents later: Gemini Spark for paid users on 2026-07-30 and free Gemini Tasks on 2026-11-17 (Korea is one of six launch countries), while Chrome auto browse and the payment/calling features remain US-only; domestic players (Naver Whale) have so far shipped assistant features with agent functions only promised "within the year".

### Cited Findings
- Gemini in Chrome (summarise, compare, draft; Gemini 3.1) rolled out to South Korea and six other APAC markets in April 2026 (Korean press ~2026-04-21); auto browse was not included. [snippet] — [Asia Economy](https://www.asiae.co.kr/en/article/2026042108250717018); [Thurrott](https://www.thurrott.com/?p=335143)
- Gemini Spark launched in Korea 2026-07-30 for Google AI Pro/Ultra subscribers, with Korean and English expanded in stages; users can give repeat-task instructions in Korean; industry view: success depends on Korean task understanding, accuracy and privacy trust. [opened] — [Daum (아이뉴스 계열 '유미's 픽') 2026-08-01](https://v.daum.net/v/20260801074138008). **Conflict: another Korean source says mid-July and AI Ultra only at KRW 119,000/mo** [snippet] — [ditoday](https://ditoday.com/%ea%b5%ac%ea%b8%80-%ec%a0%9c%eb%af%b8%eb%82%98%ec%9d%b4-%ec%8a%a4%ed%8c%8c%ed%81%ac-%ea%b5%ad%eb%82%b4-%ec%b6%9c%ec%8b%9c-24%ec%8b%9c%ea%b0%84-%ea%b0%9c%ec%9d%b8-%ec%97%90%ec%9d%b4%ec%a0%84/)
- 2026-11-17: Tasks free in South Korea, US, Canada, Japan, Australia, India; calling, Google Pay, Password Manager login, text/email instructions are US-only. [opened] — [DigitalToday 2026-11-18](https://www.digitaltoday.co.kr/en/view/127105/google-revamps-ai-agent-gemini-spark-into-tasks-adds-support-in-south-korea); [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)
- Google Korea blog (2026-05-19) covered Gemini in Chrome for Android, noting auto browse for Android planned for June (US). [snippet] — [Google Korea blog](https://blog.google/intl/ko-kr/products/android-play-hardware/bringing-chrome-ai-to-android-kr/)
- Samsung Browser agentic features (Perplexity-powered) reportedly available in South Korea and the US. [opened, tertiary] — [No Hacks](https://nohackspod.pages.dev/blog/agentic-browser-landscape-2026)
- Naver: AI탭 launched to all users 2026-06-26 and passed 10M users by 2026-07-15; Naver said it would introduce real-estate, Whale and health agents within the year; Whale "AI 챗" (page summary/translation, Q&A, tab organising, Naver Calendar) officially launched 2026-10-01; a "Whale AI browser" is promised within 2026. [snippet] — [Asia Economy 2026-07-15](https://view.asiae.co.kr/article/2026071509404690281); [CEO Score Daily 2026-10-01](https://www.ceoscoredaily.com/page/view/2026100111144358607); [NewsTomato](https://newstomato.com/ReadNews.aspx?no=1313642)
- Claude in Chrome is available on every paid Claude plan with no country restriction stated in the post. [opened] — [Anthropic](https://claude.com/blog/claude-in-chrome-generally-available)

### Inferences
- For a Korean user in Nov 2026 the realistically available browser agents are: Gemini Tasks (free, cloud VM), Claude in Chrome / Cowork browser (paid), Comet (free), ChatGPT Work (paid), Opera Neon, and Samsung Browsing Assist; Chrome auto browse and Edge Browse with Copilot are not.
- Korean-language quality is not documented by vendors; only Google is reported as explicitly staging Korean.

### Gaps
- No source on Kakao browser-agent features; no confirmation Whale agent features have shipped as of Nov 2026.
- Comet/ChatGPT Work/Claude Korean-language support statements not found.
- Samsung Korea availability comes from secondary sources only.

---

## Q6. Mobile: is browser agenting done inside the browser app or via OS screen control?

### Takeaway
On phones, the shipped products do browser agenting inside the browser app (Comet on Android/iOS, Chrome auto browse on Android, Samsung Browser) or in the vendor cloud (Gemini Tasks, ChatGPT Work), not by screen-controlling the user's live phone; Google's separate Android "task automation" runs in an on-device virtual phone. Extension-based agents (Claude in Chrome) do not exist on mobile.

### Cited Findings
- Chrome auto browse on Android is a feature of the Chrome app (AI Pro/Ultra, US, Android 12+, 4 GB RAM, en-US), hands back control for purchases and saved credentials; not on iOS. [opened] — [Android Authority](https://www.androidauthority.com/chrome-auto-browse-on-android-3665290/); [snippet] — [unite.ai](https://www.unite.ai/gemini-in-chrome-opens-to-all-u-s-android-users-as-auto-browse-goes-mobile/)
- Comet Android (2025-11-20) and iOS (2026-03-18) are free browsers with built-in assistant that can view tab contents, browse for the user and automate tasks; no extension support on mobile. [snippet] — [Thurrott](https://www.thurrott.com/mobile/333936/perplexity-launches-its-comet-browser-on-ios-and-ipados); [Engadget](https://engadget.com/apps/perplexitys-comet-ai-browser-is-now-available-for-iphones-183947569.html)
- Claude in Chrome "does not run ... on mobile yet"; mobile apps can continue/drive sessions that execute in desktop Chrome or the desktop app's built-in browser. [opened] — [Anthropic GA post](https://claude.com/blog/claude-in-chrome-generally-available); [Anthropic Cowork browser](https://claude.com/resources/articles/cowork-built-in-browser)
- Atlas was desktop-only in the sources reviewed; after retirement, mobile users reach browser agenting via ChatGPT Work's cloud browser on web/mobile. [opened] — [letsdatascience](https://letsdatascience.com/news/openai-shutters-standalone-chatgpt-atlas-desktop-browser-0cc642c2); [omidsaffari.com](https://omidsaffari.com/blog/chatgpt-agent-retired)
- Gemini Tasks is used from the Gemini app/website and executes in a per-user cloud VM; Android task automation (2026-11-10) instead runs in an on-device virtual phone. [opened] — [ppc.land](https://ppc.land/geminis-19-99-task-mode-goes-free-in-6-countries/)
- Edge mobile has Copilot multi-tab reasoning, Vision, Voice, Journeys (US) but Browse with Copilot (actions) is Edge desktop only. [opened, primary] — [Microsoft](https://blogs.windows.com/msedgedev/2026/05/13/new-updates-to-edge-across-desktop-and-mobile/)
- Gemini 3.5 Flash computer use supports mobile environments for developers. [snippet] — [Neowin](https://www.neowin.net/news/google-adds-built-in-computer-control-to-gemini-35-flash/)

### Inferences
- Mobile browser agents are in-app features that inherit that browser app's cookies/logins (class b), or cloud agents with separate sessions (class c); OS-level screen control exists only in sandboxed form (virtual phone) or as a developer API.
- iOS is the most restricted surface: only Comet ships an in-browser agent there.

### Gaps
- Technical detail of how Comet mobile or Chrome Android agents observe pages not found.
- Whether Atlas ever shipped a mobile version: sources reviewed describe desktop only; not conclusively verified.
- One source describes Android auto browse as "at the OS level" (No Hacks) vs Chrome-app feature (Android Authority); unresolved.

---

## Q7. How the market moved, 2024 to November 2026 (timeline)

### Takeaway
2024-25 was the launch phase of screenshot-driven "computer use" and standalone agent products; late 2025 was the AI-browser boom; 2026 was consolidation into general work agents, free distribution, and dual (in-profile vs isolated) architectures.

### Cited Findings
- 2024-10 Anthropic computer use beta; 2024-12 Project Mariner prototype; 2025-01-23 Operator; 2025-05 Mariner on cloud VMs (AI Ultra), Genspark browser; 2025-07 Comet, Edge Copilot Mode, ChatGPT agent (07-17); 2025-08 Claude for Chrome pilot, Operator shut; 2025-09 Opera Neon, Atlassian-TBC deal (closed 10-20); 2025-10 Comet free, Gemini 2.5 Computer Use, ChatGPT Atlas (10-21), Copilot Actions preview; 2025-11 Comet Android; 2025-12 Nova Act GA, Brave AI browsing Nightly, Claude in Chrome to Pro/Team/Enterprise, Meta-Manus deal. [memory + sources cited in Q1]
- 2026-01-28 Chrome auto browse (US, paid); 2026-03 Comet iOS, Comet enterprise, Samsung Browser for Windows with Perplexity, Mariner team reassigned; 2026-04 Gemini in Chrome in Korea/APAC; 2026-05 Mariner shut down, Copilot Mode retired (05-13), Gemini Spark (US), auto browse for Android announced; 2026-07-09 ChatGPT Work + Atlas sunset announced; 2026-07-30 Spark in 160+ countries incl. Korea; 2026-08-09 Atlas stops; 2026-08-12/26 Claude in Chrome becomes Cowork client then GA, Cowork built-in browser; 2026-08-14 Neon free plan; 2026-08-18 Gemini in Chrome for all US Android; 2026-09-01 Firefox Smart Window; 2026-10-13 Gemini Chat/Task toggle; 2026-10-15 ChatGPT extension tab takeover; 2026-11-10 Android task automation expansion; 2026-11-17 Gemini Tasks free in six countries incl. Korea. [sources cited in Q1-Q6]

### Inferences
- Direction of travel: (1) standalone -> embedded; (2) paid preview -> free mass rollout; (3) per-action approval -> classifier-gated autonomy (Anthropic) with human confirmation kept for payments/credentials (Google, OpenAI); (4) agent-payment protocols (AP2, ChatGPT Wallet 2026-11-13) appearing alongside.

### Gaps
- Dates for 2026 events rely mostly on secondary sources; see per-item flags above.
