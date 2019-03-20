package org.animetwincities.animedetour.privacypolicy

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_privacy_policy.*
import org.animetwincities.animedetour.R
import org.animetwincities.animedetour.framework.BaseFragment
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent

private const val PRIVACY_POLICY_URL = "https://www.freeprivacypolicy.com/privacy/view/6f5a2430b5b3781a05947d2517106c4b"

/**
 * Fragment for displaying the terms of service
 */
class PrivacyPolicyFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_privacy_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wv_privacy_policy?.loadUrl(PRIVACY_POLICY_URL)
    }

    override fun injectSelf(component: ActivityComponent?) {
        component?.inject(this)
    }




}