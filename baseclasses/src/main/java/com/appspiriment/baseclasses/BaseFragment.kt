package com.appspiriment.baseclasses

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.appspiriment.baseclasses.utils.BaseViewStates
import org.koin.android.viewmodel.ext.android.viewModelByClass
import kotlin.reflect.KClass

abstract class BaseFragment<
        out ViewModelType : BaseViewModel,
        out DataBindingType : ViewDataBinding>(
    viewModelClass: KClass<ViewModelType>,
    val layoutId: Int,
    val viewModelVarId: Int
) : Fragment() {

    /***************************************
     * Declarations
     ***************************************/
    val viewModel by viewModelByClass<ViewModelType>(viewModelClass)
    protected var menu: Menu? = null


    /***************************************
     * Setting Observers
     ***************************************/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding =
            DataBindingUtil.inflate(inflater, layoutId, container, false) as DataBindingType
        binding.setVariable(viewModelVarId, viewModel)
        binding.lifecycleOwner = this

        setHasOptionsMenu((getMenuId() != R.menu.main))
        retainInstance = true

        return binding.root
    }


    /**
     * *************************************
     * OnOptionsMenu Create
     * **************************************
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(getMenuId(), menu)
        this.menu = menu
    }

    /**
     * *************************************
     * OnOptionsMenu Item Selected Method
     * **************************************
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            navigateBackToHome()
        } else {
            handleOptionsItemClick(item.itemId)
        }
    }

    /**
     * *************************************
     * OnOptionsMenu Item Selected Method
     * **************************************
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intializeViews()

        setObservers()

        initializeAfterObserving()

    }

    /**
     * *************************************
     * OnOptionsMenu Item Selected Method
     * **************************************
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        navigateBackToHome()
                    }
                })
    }

    /***************************************
     * Setting Observers
     ***************************************/
    open fun <T> setBaseObserver(liveData: LiveData<T>, vararg functions: (value: T) -> Unit) {
        liveData.observe(viewLifecycleOwner, Observer { value ->
            for (function in functions)
                function(value)
        })
    }

    /***************************************
     * Setting Observers
     ***************************************/
    private fun setObservers() {
        setBaseObserver(viewModel.viewState, { state ->
            run {
                when (state) {
                    BaseViewStates.STATE_FINISH -> activity?.finish()
                    BaseViewStates.STATE_FINISH_WITH_DIALOG ->
                        showConfirmFinishActivityDialog()
                    else -> observeViewState(state)
                }

                if (state != BaseViewStates.STATE_NONE)
                    viewModel.setViewState(BaseViewStates.STATE_NONE)
            }
        })

        setBaseObserver(viewModel.navigate, { viewId ->
            if (viewId != BaseViewStates.STATE_NONE) {
                val action = getNavDirectionsAction(viewId)
                navigateToDestination(action!!)
                viewModel.setNavigate(BaseViewStates.STATE_NONE)
            }
        })

        setViewObservers()
    }

    /***************************************
     * Setting Observers
     ***************************************/
    protected fun navigateToDestination(action: NavDirections) {
        findNavController().navigate(action)
    }

    /***************************************
     * Setting Observers
     ***************************************/
    open fun getNavDirectionsAction(viewId: Int): NavDirections? {
        return null
//        return HomeFragmentDirections.actionNavHomeToNavReading(NavActions.SCRUM_GUIDE)
    }

    /**
     * *************************************
     * OnOptionsMenu Item Selected Method
     * **************************************
     */
    open fun navigateBackToHome(): Boolean {
        return true
    }


    /***************************************
     * Setting Observers
     ***************************************/
    open fun getNavigateToHomeArgs() = "none"
    open fun observeViewState(state: Int) {}
    open fun showConfirmFinishActivityDialog() {}
    open fun setViewObservers() {}
    open fun initializeAfterObserving() {}
    open fun intializeViews() {}
    open fun handleOptionsItemClick(menuItemId: Int) = false
    open fun getMenuId() = R.menu.main
}