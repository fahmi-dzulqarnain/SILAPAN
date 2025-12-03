//
// Created by Fahmi Dzulqarnain on 13/10/2025.
//

import ComposeApp
import UIKit

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        KoinSharedKt.doInitKoin()
        return true
    }
}
