# Deployed security controls in commercial screen/GUI-operating AI agents (state as of 2026-11-18)

Reading notes for the report writer:
- Every bullet carries its source and the source's date. "Fetched" = I read the page itself on 2026-11-18; "search summary" = I only saw a search-engine summary of the page (lower confidence, wording may be paraphrased).
- Several primary pages could not be fetched (openai.com and androidauthority.com returned HTTP 403). Claims resting on those are marked.
- Product-by-product findings are under Key Question 1; Key Questions 2 and 3 reuse them and add only what is specific to enforcement layer and gaps.

## Key Question 1 — What mechanisms have vendors shipped, and how do they classify (isolation / scope restriction / user consent / model-level / transparency & audit / usage limits / data handling)?

### Takeaway
By November 2026 every major vendor ships the same five-layer pattern (isolation, scope allowlist, human confirmation or takeover at login/payment, a second "critic/classifier" model, live view plus log), but they differ sharply in where the isolation lives: Google/Android (background virtual display + privileged permission), Microsoft (separate OS account and session) and OpenAI/Google cloud agents (remote VM) isolate at platform level, while Anthropic's desktop computer use and most in-profile browser agents explicitly have no sandbox and rely on permissions plus model-side checks. Structured interfaces (AppFunctions, App Intents, MCP/A2A, China's SAEP) are positioned by Google, Apple, Microsoft, Anthropic and ByteDance as the preferred path, with screen control as fallback.

### Cited Findings

#### 1. Google — Android (Gemini screen automation, Computer Control, AppFunctions)

Android Computer Control framework (developer doc, fetched; page is labelled "Preview", no publication date shown):
- Scope of who may be an agent: the framework "allows OEM-preloaded AI assistants to perform task automation on selected apps"; "An assistant app must have the privileged `ACCESS_COMPUTER_CONTROL` permission." — [Android Developers: Android Computer Control](https://developer.android.com/ai/computer-control)
- Isolation: it "enables OEM preloaded assistants to launch target apps in a secure background virtual display and operate them"; "the system runs target apps on a virtual device, similar to casting." — [Android Developers](https://developer.android.com/ai/computer-control)
- Session limits: a session covers "a set of up to six target apps for sequential execution. The system allows one active session at a given time." — [Android Developers](https://developer.android.com/ai/computer-control)
- Consent: "When an assistant app requests a session, the framework implicitly triggers a system dialog" asking the user to allow automation of the requested target apps; shown automatically the first time a target app is touched. — [Android Developers](https://developer.android.com/ai/computer-control)
- Takeover: "The assistant can hand over control to the user for manual intervention or let the user manually request to take over. For example, this is useful for transaction confirmation." — [Android Developers](https://developer.android.com/ai/computer-control)
- No app-side opt-in is required ("You don't need to make any additional changes to integrate a target app"); "An AI assistant may choose to limit the set of target apps it automates"; "At this time, the Computer Control framework can interact with a limited set of target apps." The page documents no app-side opt-out mechanism. — [Android Developers](https://developer.android.com/ai/computer-control)
- Implementation detail from code analysis (search summary; article 403 on fetch): each session uses a single trusted virtual display separate from the user's screen plus virtual input devices; the trusted display can be mirrored to a separate interactive display; only apps trusted by Google or the OEM get the permission; user approves for one session or all future sessions. — [Android Authority: Computer Control feature](https://www.androidauthority.com/android-computer-control-feature-3603862/) (2025)

Gemini screen automation as shipped (Google help page, fetched 2026-11-18; undated live page):
- Availability: Pixel 10 and Pixel 11 series, Galaxy S26 series, Z Flip 8, Z Fold 8, Tab S12 series; India, Japan, South Korea, UK, US; English, Japanese, Korean; "Be 18 or over"; personal accounts only ("isn't available if you sign in to a work, school, or supervised Google Account"); beta. — [Pixel Phone Help: Ask Gemini to handle your multi-step tasks in select Android apps](https://support.google.com/pixelphone/answer/16940971?hl=en)
- Per-app consent with three states — "Always allow," "Ask every time," "Do not allow" — in Gemini app > Settings > Screen automation; the permission "allows Gemini to read and interact with an app's screen content." — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- Isolation and live view: Gemini works in "an on-device virtual phone environment" and continues in the background; "View progress" shows it live; "Stop task" in chat or in the live-update notification. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- "Take control" is not available on Pixel 10 or Galaxy S26 (stop only); on Z Flip 8 / Z Fold 8 Gemini stops if the user does not take over within 10 minutes of a takeover request. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- Confirmations: Gemini "aims to" ask for review before certain actions (sending messages, changing a delivery address); the user performs the final step (confirming a ride, placing an order); Gemini may ask the user to take control for payment details, finalizing transactions, signing in or creating accounts. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- Usage limits: daily request limits and limits on simultaneous tasks exist; no numbers are published. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- Vendor warnings: user is "responsible for Gemini's actions during a task, including mistakes and unexpected results like purchases"; explicit prompt-injection warning (hidden instructions in app content, emails or websites could cause orders, address changes, messages); "Do not enter sign-in info, payment details, or other sensitive info directly into the chat"; avoid for emergencies or sensitive tasks. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- Data: during automation Gemini takes screenshots that can include anything visible in the app (search summary of the Google help pages; the fetched Pixel page itself defers data handling to a separate page). — [Googlebook Help: multi-step tasks in select apps](https://support.google.com/googlebook/answer/18429222?hl=en)
- First rollout, 12 Mar 2026 (fetched): Galaxy S26 series first; launch apps Lyft, Uber, Grubhub, DoorDash, Uber Eats, Starbucks; Gemini stops before placing the order and notifies with a strong vibration; in one test the preview locked the phone into a fullscreen view exitable only by forced reboot. — [9to5Google, 2026-03-12](https://9to5google.com/2026/03/12/gemini-android-app-automation-galaxy-s26-rollout/)

Server-delivered target-app allowlist:
- As of 12 Nov 2026 the list held 114 apps on stable Android 17 plus 20 in testing (134 total); Google keeps the list on its servers and the phone downloads it; each entry pairs an app ID with its signing certificate; Google can change it at any time. Categories: food/grocery, rides, travel, tickets, shopping, and work apps (Word, Excel, Outlook, Teams, Slack). No banking apps are mentioned. — [tech-ish, 2026-11-16](https://tech-ish.com/2026/11/16/gemini-task-automation-galaxy-apps/) (fetched; secondary outlet). Corroborated by title only: [Android Authority: "Exclusive: Google's hidden list of 134 apps that support Gemini task automation"](https://www.androidauthority.com/google-gemini-task-automation-target-apps-list-3734443/) (403 on fetch).

Google's stated security framework for Gemini Intelligence on Android (Google blog, 12 May 2026, fetched):
- "Gemini can only access the apps you allow it to work in, and not the rest of your device"; per-app automation toggles were promised for "later this year". — [Google blog, Kleidermacher, 2026-05-12](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- "Your Gemini assistant starts to automate a task only when you tell it to"; "Gemini is designed to require user confirmation before making purchases on your behalf." — [Google blog, 2026-05-12](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- Transparency: "View progress" plus a notification chip at the top of the screen "that you can't dismiss" while a task runs. — [Google blog, 2026-05-12](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- Audit: Google will "soon" extend the Android Privacy Dashboard to show which AI assistants were active and which apps they used in the last 24 hours (future tense in May 2026). — [Google blog, 2026-05-12](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- Model-level: Google is "building new safeguards into Android for when Gemini takes action on your behalf", compared to Chrome auto browse protections (no technical detail). Private Compute Core, Private AI Compute and protected KVM are named for ambient-data features such as Magic Cue, not specifically for screen automation. — [Google blog, 2026-05-12](https://blog.google/security/android-gemini-intelligence-security-privacy/)
- Android 17 QPR2 Beta 4 reportedly contains a privacy dashboard for tracking AI agents installed/running, and a "Data logging" menu for Private Compute Core (search summary; pre-release code analysis). — [Android Authority: Android 17 QPR2 Beta 4 agent dashboard](https://androidauthority.com/android-17-qpr2-beta-4-agent-dashboard-3704775)

AppFunctions (structured alternative):
- Callers need `android.permission.EXECUTE_APP_FUNCTIONS` (variants `_TRUSTED` / `_SYSTEM` appear in different reference versions) to execute another app's functions; an app can always execute its own. — [Android Developers: AppFunctionManager reference](https://developer.android.com/reference/android/app/appfunctions/AppFunctionManager) (search summary)
- A zh-TW developer page dated May 2026 said AppFunctions integration with Gemini was still an unlisted pre-release for trusted testers (search summary, paraphrase). — [Android Developers: AppFunctions overview](https://developer.android.google.cn/ai/appfunctions?hl=zh-tw)
- Gemini screen automation "won't work on apps available as Connected Apps" — i.e. where a structured integration exists, Google routes around the GUI. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)

#### 2. Google — Gemini in Chrome (auto browse), Gemini Agent, Project Mariner

Architecture post (Chrome security team, 8 Dec 2025, fetched):
- User Alignment Critic: "a separate model built with Gemini that acts as a high-trust system component"; it "runs after the planning is complete to double-check each proposed action" and sees "only metadata about the proposed action", so page content cannot poison it directly; it can veto, and repeated failures return control to the user. — [Google: Architecting Security for Agentic Capabilities in Chrome, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)
- Agent Origin Sets: "Read-only origins are those from which Gemini is permitted to consume content"; "Read-writable origins are those on which the agent is allowed to actuate (e.g., click, type)"; a "trustworthy gating function decides which origins proposed by the planner are relevant to the task"; the planner "cannot add new origins without the gating function's approval". Model-generated URLs are checked against a deterministic list of known public URLs. — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)
- Prompt-injection classifier "checks every page it sees for indirect prompt injection", in parallel with planning, and can block actions. — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)
- Confirmations: before navigating to banking/medical sites (deterministic list); before Chrome signs in via Google Password Manager (the model has no direct access to passwords); before purchases, payments, messaging. Work log of each step; pause/take over/stop. — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)
- Automated red-teaming with generated malicious sandboxed sites; bug bounty "up to $20,000" for breaches of the agentic security boundaries. — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)
- Stated limit: "We've initially implemented a simpler version of origin gating that just tracks the read-writeable set"; the classifier "cannot flag everything". — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)

Chrome help page (fetched 2026-11-18; live page):
- Eligibility: 18+, US, paid AI plan (Ultra/Pro/Plus), Safe Browsing Enhanced or Standard; not in Incognito, not for school accounts; work/school access "must be enabled by your administrator." — [Chrome Help: auto browse](https://support.google.com/chrome/answer/16821166?hl=en)
- Confirmation before starting a task and for "Sending communications," "Modifying your data," "Submitting web forms," "Scheduling events," and sites with highly sensitive financial or health data; may ask the user to take over for finalizing financial transactions, accepting terms of service, creating an account; pauses for sign-in. — [Chrome Help](https://support.google.com/chrome/answer/16821166?hl=en)
- "Google doesn't share your passwords with Gemini in Chrome or other sites." — [Chrome Help](https://support.google.com/chrome/answer/16821166?hl=en)
- In the local browser "Gemini has access to all the same sites that you do, including sites you're signed into"; a remote browser can continue after the device is closed. Visited sites appear in Chrome history with an action icon; chats in Gemini Apps Activity. — [Chrome Help](https://support.google.com/chrome/answer/16821166?hl=en)
- "You're responsible for Gemini's actions during a task, including mistakes and unexpected results like purchases"; safeguards "do not guarantee protection against all risks"; "experimental feature". — [Chrome Help](https://support.google.com/chrome/answer/16821166?hl=en)

Cloud-VM agents:
- Project Mariner moved to cloud VMs in May 2025 (each task in its own browser on a cloud VM); Gemini Agent mode shows a live browser on a Google VM next to the chat. Reports say Google ended Project Mariner on 4 May 2026 and folded it into Gemini Agent; other pages still describe it as live — status is conflicting (search summaries only). — [TechCrunch, 2025-05-20](https://techcrunch.com/2025/05/20/google-rolls-out-project-mariner-its-web-browsing-ai-agent/); [WebProNews on shutdown](https://www.webpronews.com/google-shuts-down-project-mariner-as-browser-agents-give-way-to-smarter-ai-workflows/)

#### 3. Samsung — One UI 9

- "AI assistant activity" (Samsung support page updated 9 Sep 2026, fetched): Settings > Security and privacy > AI assistant activity > choose assistant (example: Google); timestamped timeline in "Last 24 hours" / "Last 7 days"; Samsung calls it a "centralized audit ledger"; logs fill only when an agentic task runs (example: Gemini creating an entry in Samsung Reminder; Notes, Clock, Gallery, Calendar, Reminder named). The page describes viewing only — no delete, export, pause or block controls. — [Samsung Support (IN): How to use AI assistant activity](https://www.samsung.com/in/support/mobile-devices/how-to-use-ai-assistant-activity-feature-in-galaxy-devices/)
- Debuted with One UI 9 on Galaxy Z Fold8 / Fold8 Ultra / Flip8 (search summary). — [Android Authority: One UI 9 AI Assistant Activity dashboard](https://www.androidauthority.com/samsung-one-ui-9-ai-assistant-activity-dashboard-3690155/)
- Samsung says One UI 9 builds on Knox; no source found describing the dashboard or agent controls as a Knox feature (search summary). — [Lowyat.net, 2026](https://www.lowyat.net/2026/399365/samsung-one-ui-9-galaxy-ai-privacy/)
- On Galaxy the screen-operating agent is Google's Gemini through Android Computer Control, so isolation and consent are the Google mechanisms above; S26 owners can stop but not take over a task. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)

#### 4. Apple — Siri AI (iOS 27)

- Announced 8 Jun 2026: on-screen awareness (answering about what is on screen) and actions across apps; runs on Apple Foundation Models on device and, when needed, Private Cloud Compute, where "personal data is not stored or made accessible to Apple or anyone else"; a system orchestrator uses the Spotlight index and "App Toolbox" on device; beta "later this year", English first; not on iPhone/iPad in the EU initially; not available in China. — [iClarified reproducing Apple's announcement, 2026-06-08](https://www.iclarified.com/101113/apple-introduces-siri-ai-with-personal-context-and-onscreen-awareness) (fetched; secondary)
- App actions go through developer-declared interfaces (App Intents; on-screen-awareness / view annotation APIs) rather than raw screenshots sent to an external model vendor (search summary of a non-primary blog; one other outlet claims the model "reads the screen like a person would" — conflicting). — [mer.vin, 2026-06](https://mer.vin/2026/06/apple-wwdc26-siri-ai-and-gemini-backed-foundation-models-on-device-and-private-cloud-compute/)

#### 5. OpenAI — Operator / ChatGPT agent / Atlas

- Operator launch (Jan 2025; search summary of OpenAI's post, openai.com not fetchable): runs in a remote browser; takeover mode — "Operator asks the user to take over when inputting sensitive information into the browser, such as login credentials or payment information", and in takeover mode it does not collect or screenshot what the user enters; confirmation before significant actions (submitting an order, sending an email); watch mode on sensitive sites such as email or financial services; trained to decline banking transactions and high-stakes decisions; a "monitor model" watches for suspicious behaviour and can pause the task; "no system is flawless". — [OpenAI: Introducing Operator](https://openai.com/index/introducing-operator/)
- ChatGPT agent (Jul 2025; search summary): virtual computer; asks permission before consequential actions; user can interrupt, take over the browser, or stop; login requires the user to take control of the virtual browser. — [OpenAI: Introducing ChatGPT agent](https://openai.com/index/introducing-chatgpt-agent/); [OpenAI Help: ChatGPT agent](https://help.openai.com/en/articles/11752874-chatgpt-agent)
- Atlas (Oct 2025): logged-out mode (agent acts without the user's credentials); Watch Mode on sensitive sites, pausing if the user leaves the tab. — [Simon Willison quoting OpenAI CISO Dane Stuckey, 2025-10-22](https://simonwillison.net/2025/Oct/22/openai-ciso-on-atlas/)
- Atlas hardening (Dec 2025): LLM-based automated attacker trained end-to-end with reinforcement learning to find multi-step injections; adversarially trained agent model shipped to all Atlas users; prompt injection described as a long-term challenge "unlikely to ever be fully solved" (search summaries). — [OpenAI: Hardening Atlas against prompt injection](https://openai.com/index/hardening-atlas-against-prompt-injection); [Fortune, 2025-12-23](https://fortune.com/2025/12/23/openai-ai-browser-prompt-injections-cybersecurity-hackers/)
- Lockdown Mode and "Elevated Risk" labels (13 Feb 2026; search summary): optional setting for higher-risk users that disables or limits network-enabled tools to reduce injection-driven exfiltration (browsing limited to cached content; Deep Research disabled per a secondary summary); labels cover ChatGPT, Atlas and Codex; a 4 Jun 2026 update says it is rolling out to personal and self-serve Business accounts. — [OpenAI: Introducing Lockdown Mode and Elevated Risk labels](https://openai.com/index/introducing-lockdown-mode-and-elevated-risk-labels-in-chatgpt/); [Help Net Security, 2026-02-16](https://helpnetsecurity.com/2026/02/16/chatgpt-lockdown-mode-elevated-risk)

#### 6. Anthropic — computer use tool, Claude in Chrome, Cowork computer use

Computer use tool, API (docs fetched 2026-11-18):
- Guidance, not enforcement: "Using a dedicated virtual machine or container with minimal privileges"; avoid giving login information; "Limiting internet access to an allowlist of domains"; "Asking a human to confirm decisions that might result in meaningful real-world consequences". — [Claude Platform docs: computer use tool](https://platform.claude.com/docs/en/agents-and-tools/tool-use/computer-use-tool)
- Built-in: "classifiers automatically scan what the tool returns, such as screenshots, to flag potential prompt injections... they steer the model to check whether the instruction really came from you before acting on it"; opt-out only via support. — [Claude Platform docs](https://platform.claude.com/docs/en/agents-and-tools/tool-use/computer-use-tool)
- Data: "Client-side tool where screenshots and files are captured and stored in your environment, not by Anthropic" (ZDR-eligible). — [Claude Platform docs](https://platform.claude.com/docs/en/agents-and-tools/tool-use/computer-use-tool)

Claude in Chrome (help page fetched 2026-11-18, "Updated today"):
- Three permission modes: Manual ("Manually approve", formerly "Ask before acting"); Auto ("Automatically approve" — Claude reviews each action for safety and blocks unsafe ones; default in the Cowork side panel); Skip ("Skip all approvals", formerly "Act without asking" — nothing checks actions). — [Claude Help: Manage Claude in Chrome permissions](https://support.claude.com/en/articles/12902446-manage-claude-in-chrome-permissions)
- Site-level consent: "Allow this action" / "Always allow actions on this site" / "Decline"; approved-site list and permission history are reviewable and revocable. — [Claude Help](https://support.claude.com/en/articles/12902446-manage-claude-in-chrome-permissions)
- Always-ask actions in any mode: modifying permission settings, granting authorizations, entering potentially sensitive information, permanent deletions, downloading files. — [Claude Help](https://support.claude.com/en/articles/12902446-manage-claude-in-chrome-permissions)
- Prohibited regardless of permissions: purchases or financial transactions, creating accounts, handling credit-card/ID data, financial trades, modifying system files, "completing instructions from emails or web content", bypassing bot checks. — [Claude Help](https://support.claude.com/en/articles/12902446-manage-claude-in-chrome-permissions)
- Admin allowlists/blocklists on Team and Enterprise. — [Claude Help](https://support.claude.com/en/articles/12902446-manage-claude-in-chrome-permissions)
- Blocked site categories: help-centre versions conflict — an older one lists financial services, banking, investment/trading, crypto exchanges, adult and pirated content as blocked; a newer one says Claude asks permission before financial sites and blocks only adult and known pirated sites (search summary; not resolved). — [Claude Help: Use Claude in Chrome safely](https://support.claude.com/en/articles/12902428-use-claude-in-chrome-safely)

Published attack-success rates:
- Pilot, Aug 2025: 123 test cases / 29 scenarios; 23.6% attack success without mitigations, 11.2% with mitigations in autonomous mode (search summary). — [Anthropic: Piloting Claude for Chrome, 2025-08-26](https://www.anthropic.com/news/claude-for-chrome)
- 24 Nov 2025 (fetched), internal adaptive "Best-of-N" attacker, 100 attempts per environment; model only / old safeguards / improved safeguards: Sonnet 4 13.8% / 10.7% / 2.0%; Sonnet 4.5 13.8% / 10.8% / 1.9%; Haiku 4.5 12.0% / 11.1% / 2.4%; Opus 4.5 8.0% / 7.6% / 1.4%. Defences: RL training on simulated injected web content, classifiers scanning all untrusted content (hidden text, manipulated images, deceptive UI), red teaming. "A 1% attack success rate—while a significant improvement—still represents meaningful risk." — [Anthropic: prompt injection defenses, 2025-11-24](https://www.anthropic.com/research/prompt-injection-defenses)

Cowork computer use on the desktop (help page fetched 2026-11-18, "Updated yesterday"; labelled beta; Pro/Max/Ultra plans only):
- Tool priority: connectors first, then browser, then screen interaction as last resort. — [Claude Help: Let Claude use your computer in Cowork](https://support.claude.com/en/articles/14128542-let-claude-use-your-computer-in-cowork)
- "Claude asks before accessing each application"; "some sensitive apps (investment and trading platforms, cryptocurrency) are blocked by default"; user app blocklist auto-denies requests. — [Claude Help](https://support.claude.com/en/articles/14128542-let-claude-use-your-computer-in-cowork)
- "Computer use has no sandbox between Claude and your applications." — [Claude Help](https://support.claude.com/en/articles/14128542-let-claude-use-your-computer-in-cowork)
- An automated action check scans for signs of prompt injection; it sees the latest screenshot and "Anthropic can keep a copy of it in its record of the check"; guardrails "aren't absolute"; advice to keep banking, healthcare and government apps out. — [Claude Help](https://support.claude.com/en/articles/14128542-let-claude-use-your-computer-in-cowork)

#### 7. Microsoft — Windows agent workspace, MCP on Windows, Edge

Experimental agentic features (support page fetched; published 16 Oct 2025, updated 17 Nov and 5 Dec 2025):
- "The experimental agentic features setting is off by default"; "can only be enabled by an administrative user of the device"; once on it applies to all users. — [Microsoft Support: Experimental agentic features](https://support.microsoft.com/en-us/windows/ai/ai-features/experimental-agentic-features)
- "Each agent operates using its own account, distinct from your personal user account"; "agent workspace runs in a separate Windows session"; "Each agent has its own workspace and its own permissions". — [Microsoft Support](https://support.microsoft.com/en-us/windows/ai/ai-features/experimental-agentic-features)
- Known-folder access (Documents, Downloads, Desktop, Videos, Pictures, Music); on builds 26100.7344+ per agent: Allow Always / Ask every time / Never allow. — [Microsoft Support](https://support.microsoft.com/en-us/windows/ai/ai-features/experimental-agentic-features)
- Principles: "All actions of an agent are observable and distinguishable from those taken by a user"; privileges "granular, specific and time bound"; "Windows should be able to verify these actions with a tamper-evident audit log" (principle; implementation not described). Risk named: "cross-prompt injection (XPIA)". — [Microsoft Support](https://support.microsoft.com/en-us/windows/ai/ai-features/experimental-agentic-features)
- Copilot Actions apps run under the agent account, not the signed-in user's (search summary). — [Microsoft Learn: Securing AI agents on Windows](https://learn.microsoft.com/en-us/windows/security/book/operating-system-agentic-security)
- Nov 2026 docs restructure agent security as "Agent identity" and "Agent governance"; Microsoft Execution Containers (MXC) "provide policy-driven runtime boundaries that help control file and network access while agents run". — [Microsoft Learn: Agent governance, ms.date 2026-11-04](https://learn.microsoft.com/en-us/windows/security/application-security/agent-governance); [Microsoft Learn: Application and agent security, ms.date 2026-11-14](https://learn.microsoft.com/en-us/windows/security/application-security/)

MCP on Windows (search summaries):
- Agent connectors in the Windows on-device registry sit behind an MCP proxy that handles consent, governance, auditing and containment enforcement; servers need code signing, packaging identity and declared capabilities to be listed. — [Windows Experience Blog, 2025-11-18](https://blogs.windows.com/windowsexperience/2025/11/18/preparing-for-whats-next/)
- By default such servers "are run in an agent session, securely contained in a separate environment, and can only be allowed access to approved resources"; a testing toggle "Reduce protections for agent connectors" removes this. — [Microsoft Learn: MCP containment](https://learn.microsoft.com/en-nz/windows/ai/mcp/servers/mcp-containment)

Edge Copilot Mode Actions (search summary of Microsoft's post, 23 Oct 2025):
- Default "Balanced Mode" allows a curated site list; "Strict Mode" requires approval for every site; page data labelled untrusted and screened with Azure Prompt Shields; checks for hidden instructions and task drift with confirmation when risk is higher; no interaction with edge:// pages and no access outside Edge. — [Microsoft Edge Blog: Considerations for Safe Agentic Browsing, 2025-10-23](https://blogs.windows.com/msedgedev/2025/10/23/considerations-for-safe-agentic-browsing/)

#### 8. Other agentic browsers

- Brave AI browsing (blog updated 5 May 2026; search summary): separate browser profile with its own cookies, logins, caches; an "alignment checker" second model receives system prompt, user prompt and task model's response; no access to internal pages, non-HTTPS pages or Safe Browsing-flagged sites; opt-in feature flag. Brave states the checker "cannot eliminate all risks" and can itself be targeted via the task model's output. — [Brave: AI browsing](https://brave.com/blog/ai-browsing/)
- Perplexity Comet: BrowseSafe content classifier (Perplexity claims about 91% detection; red-teamers found weakness against encoding-based injections); tool permissions limited by default; confirmation for sensitive actions; Perplexity reports its detector flagged 1.15 million injections between Sep 2025 and Sep 2026 (search summaries). — [Perplexity: BrowseSafe++](https://perplexity.ai/hub/blog/browsesafe-plus-plus); [arXiv 2511.20597](https://arxiv.org/html/2511.20597v1); [eSecurity Planet: Red-teaming BrowseSafe](https://www.esecurityplanet.com/threats/red-teaming-browsesafe-exposes-ai-browser-guardrail-gaps/)

#### 9. China — Doubao phone and regulation

- First generation (Nubia M153, Dec 2025): read-screen plus simulated taps; within days WeChat logins failed and Taobao and bank apps triggered risk-control checks; ByteDance withdrew finance and some sensitive operations. — [36Kr, 2026-09](https://eu.36kr.com/zh/p/3986222650283657) (fetched)
- Second generation (Nubia NaviX Ultra, on sale 16 Sep 2026): "protocol first, simulation as fallback, human confirmation" — (a) apps with MCP/A2A execute the task themselves; (b) apps without a protocol that consent to simulated taps are operated under SAEP, with login, payment and lottery/check-in pages off-limits and payment done by the user; (c) apps that refuse are only opened for the user. — [36Kr, 2026-09](https://eu.36kr.com/zh/p/3986222650283657)
- SAEP (Screen Automation Operation Declaration Protocol, published by Doubao about two days before launch): apps declare boundaries by page, business intent and operation type; the system combines agent identity, security baseline, app declaration and user authorization; 30-day notice period during which third-party apps are not operated by default; explicit refusals always honoured. — [36Kr, 2026-09](https://eu.36kr.com/zh/p/3986222650283657)
- 155 apps reportedly refused Doubao phone access, including ICBC, China Merchants Bank and Alipay (search summary; original outlet not fetched). — [Jiemian](https://m.jiemian.com/article/15104952.html)
- Hands-on test: payment and real-name steps are handed to the user (search summary). — [Yicai](https://www.yicai.com/news/103366765.html)
- Regulation: MIIT General Office notice on network-access management of agent terminal devices (工信厅信管〔2026〕42号, 6 Nov 2026) covers adaptation testing, permission invocation, network-access testing and OTA upgrades; press reports say app-distribution platforms must have developers declare by electronic signature whether they consent to agent invocation, and agents may not force-invoke apps that have not consented (search summary; full text not read). — [Xinhua, 2026-11-06](https://www.news.cn/20261106/b56a8a3cb14e40a69d997b37b39cc99a/c.html); [IT之家](https://ithome.com/1/023/059.htm)
- Standards reported by press (search summary, not verified against originals): "dual authorization" guideline of Dec 2025 (user plus app consent); group standard T/BFIA 070-2026 on agent security in finance (27 Aug 2026); a mandatory national standard plan on agent application security (Jul 2026). — [Cover News](https://cbgc.scol.com.cn/news/7944927)
- Alipay's agent uses a dual track — MCP for integrated mini-programs, GUI simulated taps otherwise — with single-use confirmation for payment and medical-insurance operations (search summary of a commentary piece). — [Huxiu](https://www.huxiu.com/article/4894229.html?f=rss)

#### Classification matrix (derived from the findings above)

| Class | Shipped examples |
|---|---|
| Isolation | Android background virtual display; Windows agent account + separate session, MXC; OpenAI/Gemini Agent cloud VM/remote browser; Brave separate profile; Anthropic API: VM recommended only; Cowork desktop: none |
| Scope restriction | Google server allowlist (114 apps, cert-pinned), max 6 apps/session; Chrome origin sets; Edge curated site list; Claude blocked apps/sites + admin lists; SAEP page-level declarations |
| User consent | Android system dialog + per-app Always/Ask/Never; Claude per-site/per-app; Windows per-agent folder access; confirmations before purchase/send; takeover for login/payment (Google, OpenAI, Doubao) |
| Model-level | Chrome User Alignment Critic + injection classifier; Brave alignment checker; Anthropic RL training + classifiers; OpenAI monitor model + adversarial training; Perplexity BrowseSafe; Edge Prompt Shields |
| Transparency/audit | Android non-dismissible chip + View progress; Samsung AI assistant activity (24h/7d); Chrome work log + history icon; Claude permission history; Windows audit-log principle |
| Usage limits | Android: one session, six apps; Gemini daily/concurrent limits (numbers unpublished); age 18+, personal accounts only |
| Data handling | Anthropic API screenshots stay client-side; Cowork check may retain a screenshot; OpenAI no capture in takeover; Apple PCC no storage; Chrome passwords not shared with model |

### Inferences
- Google's Android design is the only phone design found where the screen-control capability itself is a platform permission that ordinary apps cannot obtain; Chinese first-generation designs obtained equivalent power through OEM partnership without app-side consent, which is what triggered the app-vendor backlash and the SAEP/dual-authorization response.
- The industry has converged on "structured interface first, GUI last": Anthropic states the priority order outright, Google disables screen automation for Connected Apps, Doubao gen-2 is "protocol first", Apple routes through App Intents.
- Payment and login are handled almost uniformly by handing control back to the human rather than by trusting the model, which suggests vendors do not consider model-level defences sufficient for irreversible actions.
- The server-delivered, certificate-pinned allowlist gives Google a remote kill switch per app and blocks repackaged apps from impersonating a target, though the latter is the secondary outlet's reading rather than a Google statement.

### Gaps
- Exact numeric daily/concurrent limits for Gemini screen automation are not published.
- Screenshot retention and human-review policy for Gemini screen automation: the fetched help page defers to another page I did not read.
- Whether the Android 17 Privacy Dashboard AI activity view has shipped in stable: Google's May 2026 post says "soon"; only beta code sightings found.
- Whether the promised per-app automation toggles differ from the existing Gemini-app per-app permission is unclear.
- No Samsung source found for Knox-specific agent protections or for Bixby operating third-party GUIs.
- Apple: no primary Apple page read; no source describes confirmation prompts or a permission model for Siri app actions; the Gemini role in data handling is unverified.
- OpenAI primary pages were not fetchable (403); current status of ChatGPT agent is contested — secondary sources say it was retired in favour of "ChatGPT Work"; a "30-day retention" claim is unverified.
- Copilot Studio computer use (allowlists, credential handling) and Opera Neon: nothing retrieved.
- Honor, Xiaomi, OPPO, vivo: no reliable primary documentation of their sensitive-app handling was found; full texts of the MIIT notice and the Chinese standards were not read.

## Key Question 2 — Which controls are enforced by the OS/platform versus by the agent's own model or app (i.e. which survive a fully compromised model)?

### Takeaway
Controls that hold even if the model is fully hijacked are the platform-enforced ones: Android's privileged permission, virtual display, system consent dialog and session caps; Windows' separate agent account, session and ACLs; cloud-VM isolation; Chrome's deterministic origin gating, sensitive-site list and password-manager mediation; and separate-profile isolation in Brave. "Stop before payment", refusal of banking tasks, critic models and injection classifiers are model- or app-level and vendors themselves describe them as probabilistic.

### Cited Findings
- Android platform-enforced: privileged permission restricted to OEM-preloaded assistants, system-triggered consent dialog, one session and six apps, target apps running on a virtual display. — [Android Developers](https://developer.android.com/ai/computer-control)
- Android agent-app-enforced: the allowlist is the assistant's choice ("An AI assistant may choose to limit the set of target apps it automates"), delivered from Google's servers. — [Android Developers](https://developer.android.com/ai/computer-control); [tech-ish, 2026-11-16](https://tech-ish.com/2026/11/16/gemini-task-automation-galaxy-apps/)
- Android model-level and explicitly soft: Gemini "aims to" ask for review before certain actions; the help page warns of "completing unauthorized purchases". — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- Chrome: gating function and URL checks are described as deterministic or isolated from page content; the critic sees "only metadata"; the model has no direct access to stored passwords; sensitive-site confirmation uses a deterministic list. — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/)
- Chrome residual exposure: in the local browser "Gemini has access to all the same sites that you do, including sites you're signed into." — [Chrome Help](https://support.google.com/chrome/answer/16821166?hl=en)
- Windows OS-enforced: separate account, separate session, per-agent folder permissions, admin-only enablement. — [Microsoft Support](https://support.microsoft.com/en-us/windows/ai/ai-features/experimental-agentic-features)
- Windows MCP: proxy-mediated consent and containment in an agent session by default. — [Microsoft Learn: MCP containment](https://learn.microsoft.com/en-nz/windows/ai/mcp/servers/mcp-containment)
- Anthropic desktop: "Computer use has no sandbox between Claude and your applications"; in Skip mode "nothing checks its actions". — [Claude Help: Cowork computer use](https://support.claude.com/en/articles/14128542-let-claude-use-your-computer-in-cowork); [Claude Help: permissions](https://support.claude.com/en/articles/12902446-manage-claude-in-chrome-permissions)
- Anthropic API: isolation is the developer's responsibility (VM/container, domain allowlist are recommendations). — [Claude Platform docs](https://platform.claude.com/docs/en/agents-and-tools/tool-use/computer-use-tool)
- Brave: profile isolation is structural; the alignment checker can be attacked through the task model's output. — [Brave: AI browsing](https://brave.com/blog/ai-browsing/)
- Doubao gen-2: boundaries come from app declarations (SAEP) combined with agent identity and user authorization, evaluated by the system. — [36Kr, 2026-09](https://eu.36kr.com/zh/p/3986222650283657)

### Inferences
- Survives a compromised model: who can be an agent (Android permission; Windows admin toggle), which apps or origins are reachable (allowlist checked outside the model; origin gating; Edge site list; admin blocklists), what credentials the agent holds (password manager mediation; logged-out mode; separate profile/account/VM), and visibility (non-dismissible chip, OS-level logs).
- Does not survive: per-action confirmation decided by the model, task refusals, critic/alignment models and classifiers (a second model reduces but does not remove risk), and the "stop at payment" behaviour on Android, which appears to be agent behaviour rather than a framework rule — the framework only offers a hand-over primitive.
- Within an allowed app or read-writable origin, no shipped platform control limits what a hijacked model can do short of payment/credential entry; scope restriction bounds the blast radius but not in-scope misuse (e.g. changing a delivery address, sending messages).
- Android's virtual display protects the rest of the device from the agent and the user's foreground session from interference, but the target app still runs with the user's logged-in data, so it is containment of reach, not of authority.
- Client-side allowlists held by the assistant app are only as strong as that app's integrity; on Android this is backed by the assistant being a privileged preloaded app.

### Gaps
- Whether Android enforces any OS-level block on categories such as banking apps or FLAG_SECURE windows inside Computer Control sessions is not documented in the sources read.
- Whether Samsung's and Android's activity logs are written by the OS or self-reported by the assistant is not stated.
- Microsoft's tamper-evident audit log is a stated principle; I found no documentation of a shipped implementation.
- How OpenAI's watch mode and confirmations are enforced (model vs harness) is not documented in what I could access.

## Key Question 3 — What are the documented gaps or vendor caveats?

### Takeaway
Every vendor states in its own documentation that prompt injection is unsolved and shifts residual responsibility to the user; published residual attack rates are still around 1–2% under adaptive attack, and several announced controls (Android AI activity dashboard, Windows tamper-evident log, full origin gating in Chrome) were partial or pending in the sources found.

### Cited Findings
- Google Android: user is "responsible for Gemini's actions during a task, including mistakes and unexpected results like purchases"; confirmation is something Gemini "aims to" do. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- Google Android: no take-over on Pixel 10 and Galaxy S26, only stop. — [Pixel Phone Help](https://support.google.com/pixelphone/answer/16940971?hl=en)
- Google Android: preview bug locked a phone in fullscreen until forced reboot. — [9to5Google, 2026-03-12](https://9to5google.com/2026/03/12/gemini-android-app-automation-galaxy-s26-rollout/)
- Google Android: Computer Control is a "Preview" limited to OEM-preloaded assistants, so third-party agents cannot use the protected path. — [Android Developers](https://developer.android.com/ai/computer-control)
- Chrome: only "a simpler version of origin gating that just tracks the read-writeable set" was implemented initially; safeguards "do not guarantee protection against all risks". — [Google, 2025-12-08](https://blog.google/security/architecting-security-for-agentic/); [Chrome Help](https://support.google.com/chrome/answer/16821166?hl=en)
- Samsung: activity page is view-only per Samsung's own page; a third-party claim of pause/dismiss controls is unconfirmed. — [Samsung Support](https://www.samsung.com/in/support/mobile-devices/how-to-use-ai-assistant-activity-feature-in-galaxy-devices/)
- OpenAI: prompt injection "is unlikely to ever be fully solved" (search summary). — [Fortune, 2025-12-23](https://fortune.com/2025/12/23/openai-ai-browser-prompt-injections-cybersecurity-hackers/)
- OpenAI Atlas: in an independent test Watch Mode did not trigger on GitHub or an online banking site. — [Simon Willison, 2025-10-22](https://simonwillison.net/2025/Oct/22/openai-ciso-on-atlas/)
- Anthropic: "A 1% attack success rate... still represents meaningful risk"; desktop guardrails "aren't absolute"; no sandbox; the action check may retain a screenshot. — [Anthropic, 2025-11-24](https://www.anthropic.com/research/prompt-injection-defenses); [Claude Help](https://support.claude.com/en/articles/14128542-let-claude-use-your-computer-in-cowork)
- Anthropic API: "In some circumstances, Claude follows commands found in content even when they conflict with your instructions." — [Claude Platform docs](https://platform.claude.com/docs/en/agents-and-tools/tool-use/computer-use-tool)
- Microsoft: features are "experimental", may affect device security, and stricter agent-account rules are deferred to later releases; XPIA and hallucination named as risks. — [Microsoft Support](https://support.microsoft.com/en-us/windows/ai/ai-features/experimental-agentic-features)
- Brave: the alignment checker "cannot eliminate all risks". — [Brave: AI browsing](https://brave.com/blog/ai-browsing/)
- Perplexity: third-party red-teaming found BrowseSafe weak against encoded injections (search summary). — [eSecurity Planet](https://www.esecurityplanet.com/threats/red-teaming-browsesafe-exposes-ai-browser-guardrail-gaps/)
- China: SAEP after its 30-day notice period opens non-refusing apps progressively by risk tier, i.e. opt-out rather than opt-in for apps; transaction scenarios mostly stop at query or manual confirmation. — [36Kr, 2026-09](https://eu.36kr.com/zh/p/3986222650283657)

### Inferences
- Vendor attack-success numbers are self-reported on internal benchmarks and are not comparable across vendors; only Anthropic publishes per-model figures in the sources found.
- Liability language ("you're responsible") is uniform at Google and implies that confirmation UX is doing legal as well as security work.
- Audit features lag action features: activity logs arrived (Samsung, Sep 2026) or remained pending (Android dashboard) months after screen automation shipped (Mar 2026).
- App developers have no documented opt-out on Android Computer Control, in contrast with the consent-of-app direction taken in China (SAEP, MIIT notice); this is the largest design divergence between regions.

### Gaps
- No independent measurement of Android screen-automation injection resistance was found.
- No vendor documents spending caps or transaction limits for agents.
- Third-party analysis notes that Google has not disclosed which actions count as requiring confirmation on Android; I found no Google source that enumerates them beyond examples.
- Several claims above rest on search summaries rather than fetched pages (marked inline); they should be re-verified before being quoted verbatim.
