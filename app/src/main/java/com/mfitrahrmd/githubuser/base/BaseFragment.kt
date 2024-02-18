package com.mfitrahrmd.githubuser.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.mfitrahrmd.githubuser.ui.AppViewModelProvider
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>(private val _viewModelClass: Class<VM>) :
    Fragment() {
    private var _viewBinding: VB? = null
    protected val viewBinding: VB
        get() = _viewBinding!!

    protected val viewModel: VM by lazy {
        ViewModelProvider(this, AppViewModelProvider.Factory)[_viewModelClass]
    }

    protected open fun bind() {

    }

    protected open fun observe() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vbType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val vbClass = vbType as Class<VB>
        val inflate = vbClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )

        _viewBinding = inflate.invoke(null, inflater, container, false) as VB

        return _viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}