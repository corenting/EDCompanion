---
title: Commander feature
layout: page
---

# How it works

The commander tab of EDCompanion allows you to see statistics about your in-game CMDR. Multiple providers can be used to get informations from the game:
- Frontier API (directly from the game API without using any third-party services)
- [EDSM](https://edsm.net/)
- [Inara](https://http://inara.cz/)

Please note that EDSM and Inara are third-party companion websites. They are not affiliated with EDCompanion or Frontier Developments.
If you want to use them, **you need to create an account** on these service. You also need to **sync your game data to these services**, which will not be covered here. Only the official Frontier API source will automatically get informations without any extra steps.

# Why different providers ?

Each provider has different informations. You can enable multiple sources at the same time. For each data (ranks, credits, position...) the most complete source will be used.

Here is a summary of what is provided by each service:

| Provider      | Ranks                                    | Position  | Credits | Fleet tab | How to connect                                                                               |
| ------------- | ---------------------------------------- | --------  | ------- | --------- | --------------------------------------------------------------------------------------------- |
| Frontier API  | ✅ (but without the progress percentage) | ✅        | ✅     | ✅        | Secure login through the Frontier website                                                     |
| EDSM          | ✅                                       | ✅        | ✅     | ❌        | [API key from your EDSM settings](https://www.edsm.net/en/settings/api) and your CMDR name    |
| Inara         | ✅ (no mercenary or exobiologist ranks)  | ❌        | ❌     | ❌        | Your Inara account name (your profile must be public)                                         |

For example, if you enable only the Frontier API source, you will get all the data but without the ranks progress percentage. But if you also enable EDSM or Inara, you will get complete informations as the data will be merged.
