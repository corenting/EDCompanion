---
title: Commander feature
---

## How it works

The commander tab of EDCompanion allows you to see statistics about your in-game CMDR. Multiple providers can be used to get informations from the game:
- Frontier API (directly from the game API without using any third-party services)
- [EDSM](https://edsm.net/)
- [Inara](https://http://inara.cz/)

Please note that EDSM and Inara are third-party companion websites. They are not affiliated with EDCompanion or Frontier Developments.
If you want to use them, **you need to create an account** on these service. You also need to **sync your game data to these services**, which will not be covered here.

## Providers comparison

The data available varies between the different services :

| Provider      | Ranks                                    | Position  | Credits | Fleet tab | How to connect                                                                                |
| ------------- | ---------------------------------------- | --------  | ------- | --------- | --------------------------------------------------------------------------------------------- |
| Frontier API  | ✅ (but without the progress percentage) | ✅        | ✅       | ✅        | Secure login through the Frontier website                                                     |
| EDSM          | ✅                                       | ✅        | ✅       | ❌        | [API key from your EDSM settings](https://www.edsm.net/en/settings/api) and your CMDR name    |
| Inara         | ✅                                       | ❌        | ✅       | ❌        | Your Inara account name (your profile must be public)                                         |
