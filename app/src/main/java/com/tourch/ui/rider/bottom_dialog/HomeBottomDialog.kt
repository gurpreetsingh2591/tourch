package com.tourch.ui.rider.bottom_dialog

import android.content.Context
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tourch.R
import com.tourch.databinding.DialogAddPromoBinding
import com.tourch.databinding.LayoutFindingDriverBinding
import com.tourch.databinding.LayoutSelectCarBinding
import com.tourch.ui.rider.DashboardFragment
import com.tourch.ui.rider.SelectVehicleAdapter
import com.tourch.ui.rider.model.AddPromoModel
import com.tourch.ui.rider.SelectPromoAdapter
import com.tourch.ui.rider.SelectServicesAdapter
import com.tourch.ui.rider.model.EstimateFareApiResponse
import com.tourch.ui.rider.model.ServiceListApiResponse

class HomeBottomDialog {

    private lateinit var addPromoModel: AddPromoModel
    private lateinit var bSSelectVehicleDialog: BottomSheetDialog
    private lateinit var bSRideDetailDialog: BottomSheetDialog
    lateinit var bSSearchVehicleDialog: BottomSheetDialog
    private lateinit var bSAddPromoDialog: BottomSheetDialog
    private lateinit var bSSplitFareDialog: BottomSheetDialog
    private lateinit var bSSplitFareAddContactDialog: BottomSheetDialog
    private lateinit var bSYouArrivedDialog: BottomSheetDialog
    private lateinit var bSHowsYourRideDialog: BottomSheetDialog
    private lateinit var bSSafetyCenterDialog: BottomSheetDialog

    fun showSelectVehicle(
        context: Context,
        view: View,
        rides: ArrayList<EstimateFareApiResponse.Ride>,
        services: ArrayList<ServiceListApiResponse.ServiceListApiResponseItem>
    ) {
        bSSelectVehicleDialog = BottomSheetDialog(context)

        val bindingSC = LayoutSelectCarBinding.inflate(bSSelectVehicleDialog.layoutInflater)
        bSSelectVehicleDialog.setContentView(bindingSC.root)

        // R.layout.layout_select_car
        val behavior = bSSelectVehicleDialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bSSelectVehicleDialog.setCancelable(false)

        //Services Adapter
        val linearLayoutServ = LinearLayoutManager(context)
        linearLayoutServ.orientation = LinearLayoutManager.HORIZONTAL
        bindingSC.rvServiceSelection.layoutManager
        bindingSC.rvServiceSelection.layoutManager = linearLayoutServ
        val selectServicesAdapter = SelectServicesAdapter(services, object : SelectServicesAdapter.OnServicesClick {
            override fun onClick(position: Int) {
                DashboardFragment.selectedServiceId = services[position].serviceId.toString()
                DashboardFragment.callEstimateFareApi(services[position].serviceId.toString())
                bSSelectVehicleDialog.dismiss()
            }
        })
        bindingSC.rvServiceSelection.adapter = selectServicesAdapter

        var fareInfo: EstimateFareApiResponse.Ride.FareInfo? = null
        //Car adapter
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        bindingSC.rvCarsSelection.layoutManager
        bindingSC.rvCarsSelection.layoutManager = linearLayoutManager
        val selectFiltersAdapter = SelectVehicleAdapter(rides, object : SelectVehicleAdapter.OnVehicleClick {
                override fun onClick(position: EstimateFareApiResponse.Ride) {
                    fareInfo = position.fareInfo!!
                }
            })
        bindingSC.rvCarsSelection.adapter = selectFiltersAdapter

        bindingSC.tvBack.setOnClickListener {
            bSSelectVehicleDialog.dismiss()
        }

        bindingSC.llAddPromo.setOnClickListener {
            addPromoDialog(context)
        }
        bindingSC.btnBookNow.setOnClickListener {
            if (fareInfo?.baseFare != null){
                bSSelectVehicleDialog.dismiss()
                DashboardFragment.callBookApi(fareInfo)
            }else{
                Toast.makeText(context,"Please select a service!" + fareInfo, Toast.LENGTH_SHORT).show()

            }


        }

        bSSelectVehicleDialog.show()
    }

    private fun addPromoDialog(context: Context) {
        var flagIsClick = false
        addPromoModel = AddPromoModel("35", "Tourch 11", "04 Oct, 2022")
        val addPromoList: ArrayList<AddPromoModel> = arrayListOf()
        addPromoList.add(addPromoModel)

        addPromoModel = AddPromoModel("25", "Tourch 12", "06 Oct, 2022")
        addPromoList.add(addPromoModel)

        bSAddPromoDialog = BottomSheetDialog(context)

        val bindingPD = DialogAddPromoBinding.inflate(bSAddPromoDialog.layoutInflater)
        bSAddPromoDialog.setContentView(bindingPD.root)

        //R.layout.dialog_add_promo
        val behavior = bSAddPromoDialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val linearLayoutManager = LinearLayoutManager(context)
        bindingPD.rvPromo.layoutManager = linearLayoutManager
        val selectPromoAdapter =
            SelectPromoAdapter(addPromoList, object : SelectPromoAdapter.OnPromoClick {
                override fun onClick(position: Int) {
                    flagIsClick = true
                    bindingPD.btnPromoApply.background.setTint(ContextCompat.getColor(context, R.color.colorPrimary))
                }
            })
        bindingPD.rvPromo.adapter = selectPromoAdapter

        bindingPD.btnPromoApply.setOnClickListener {
            if (flagIsClick) {
                bSAddPromoDialog.dismiss()
            } else {
                //  showToast("Please select promo code")
            }
        }
        bindingPD.ivPromoClose.setOnClickListener {
            bSAddPromoDialog.dismiss()
        }

        bSAddPromoDialog.show()
    }

     fun showSearchVehicle(context: Context) {
        bSSearchVehicleDialog = BottomSheetDialog(context)
        val bindingVD = LayoutFindingDriverBinding.inflate(bSSearchVehicleDialog.layoutInflater)
        bSSearchVehicleDialog.setContentView(bindingVD.root)

        val behavior = bSSearchVehicleDialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
         bSSearchVehicleDialog.setCancelable(false)

        val timer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                //showToast("Your driver has found, Now Connecting.")
                bindingVD.tvFinding.text = "Connecting to your driver..."
                bindingVD.tvSubFinding.text = "Your phone will vibrate when driver arrive."
                bindingVD.tvPickUpTiming.visibility = View.VISIBLE
                // bSSearchVehicleDialog.dismiss()

                val timer = object : CountDownTimer(20000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        //view.visibility = View.GONE
                        bSSearchVehicleDialog.dismiss()
                      //  showDetailRide(context, view)
                    }
                }
                timer.start()
            }
        }
        timer.start()
        bSSearchVehicleDialog.show()
    }

/*
    fun showDetailRide(context: Context) {

        bSRideDetailDialog = BottomSheetDialog(context)
        val bindingDR = DialogRiderFullDetailBinding.inflate(bSRideDetailDialog.layoutInflater)
        bSRideDetailDialog.setContentView(bindingDR.root)

        val behavior = bSRideDetailDialog.behavior
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        //bSRideDetailDialog.setCancelable(false)

        */
/*bindingDR.llSplitFareFull.setOnClickListener {
            showSplitFare(context)
        }

        bindingDR.llSplitFareHalf.setOnClickListener {
            showSplitFare(context)
        }*//*


        */
/*val timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                bSRideDetailDialog.dismiss()
                //showAddRatingToRide(context, view)
            }
        }
        timer.start()*//*


        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                print(newState)
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        bindingDR.inShortDetail.titleState.text = "Show Bottom Sheet"
                    }
                    BottomSheetBehavior.STATE_EXPANDED ->
                        bindingDR.inShortDetail.titleState.text = "Close Bottom Sheet"
                    BottomSheetBehavior.STATE_COLLAPSED ->
                        bindingDR.inShortDetail.titleState.text = "Show Bottom Sheet"
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val upperState = 0.7
                val lowerState = 0.33

                if (slideOffset >= upperState) {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    bindingDR.nsvFullDetail.visibility = View.VISIBLE
                    bindingDR.inShortDetail.view.visibility = View.GONE
                }
                if (slideOffset > lowerState && slideOffset < upperState) {
                    behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                    bindingDR.nsvFullDetail.visibility = View.GONE
                    bindingDR.inShortDetail.view.visibility = View.VISIBLE
                }
                if (slideOffset <= lowerState) {
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    bindingDR.nsvFullDetail.visibility = View.GONE
                    bindingDR.inShortDetail.view.visibility = View.VISIBLE
                }
            }
        })
        bSRideDetailDialog.show()
    }
*/

    /*   private fun showSplitFare(context: Context) {
         bSSplitFareDialog = BottomSheetDialog(context)
         bSSplitFareDialog.setContentView(R.layout.dialog_split_fare)
         val behavior = bSSplitFareDialog.behavior
         behavior.state = BottomSheetBehavior.STATE_EXPANDED

         bSSplitFareDialog.ivSplitFareBack.setOnClickListener {
             bSSplitFareDialog.dismiss()
         }
         bSSplitFareDialog.btnNext.setOnClickListener {
             if (bSSplitFareDialog.edSplitFare.text.isNotEmpty()) {
                 showSplitFareAddContact(context)
             }
         }

         bSSplitFareDialog.edSplitFare.addTextChangedListener(object : TextWatcher {
             override fun afterTextChanged(s: Editable) {
                 Log.d("ADD STOP", "afterTextChanged  $s")

             }

             override fun beforeTextChanged(
                 s: CharSequence,
                 start: Int,
                 count: Int,
                 after: Int
             ) {


             }

             override fun onTextChanged(
                 s: CharSequence,
                 start: Int,
                 before: Int,
                 count: Int
             ) {

                 Log.d("ADD STOP", "onTextChanged  $s")

                 if (bSSplitFareDialog.edSplitFare.text.isNotEmpty()) {
                     bSSplitFareDialog.btnNext.background.setTint(
                         ContextCompat.getColor(
                             context,
                             R.color.colorPrimary
                         )
                     )
                 } else {
                     bSSplitFareDialog.btnNext.background.setTint(
                         ContextCompat.getColor(
                             context,
                             R.color.color_grey
                         )
                     )
                 }
             }
         })

         bSSplitFareDialog.show()
     }

     private fun showSplitFareAddContact(context: Context) {
         bSSplitFareAddContactDialog = BottomSheetDialog(context)
         bSSplitFareAddContactDialog.setContentView(R.layout.dialog_split_fare_add_contact)
         val behavior = bSSplitFareAddContactDialog.behavior
         behavior.state = BottomSheetBehavior.STATE_EXPANDED



         bSSplitFareAddContactDialog.show()
     }

     private fun showAddRatingToRide(context: Context, view: View) {
         bSYouArrivedDialog = BottomSheetDialog(context)
         bSYouArrivedDialog.setContentView(R.layout.dialog_you_arrived)
         val behavior = bSYouArrivedDialog.behavior
         behavior.state = BottomSheetBehavior.STATE_EXPANDED
         view.visibility = View.VISIBLE
         RiderDashboardFragment.btn_continue.visibility = View.GONE
         RiderDashboardFragment.rl_TvDropOff.visibility = View.GONE
         RiderDashboardFragment.tvEnterDesti.setText("")
         RiderDashboardFragment.tilPickUpLay.hint = context.getString(R.string.enter_your_destination)
         bSYouArrivedDialog.btnRatingSubmit.setOnClickListener {
             bSYouArrivedDialog.dismiss()
             showHowsYourRide(context, view)
         }

         bSYouArrivedDialog.show()
     }

     private fun showHowsYourRide(context: Context, view: View) {
         bSHowsYourRideDialog = BottomSheetDialog(context)
         bSHowsYourRideDialog.setContentView(R.layout.dialog_hows_your_ride)
         val behavior = bSHowsYourRideDialog.behavior
         behavior.state = BottomSheetBehavior.STATE_EXPANDED

         bSHowsYourRideDialog.btnSubmit.setOnClickListener {
             bSHowsYourRideDialog.dismiss()

         }

         bSHowsYourRideDialog.show()
     }

      fun showSafetyCenterRide(context: Context) {
         bSSafetyCenterDialog = BottomSheetDialog(context)
         bSSafetyCenterDialog.setContentView(R.layout.layout_safety_center)
         val behavior = bSSafetyCenterDialog.behavior
         behavior.state = BottomSheetBehavior.STATE_EXPANDED

         bSSafetyCenterDialog.ivClose.setOnClickListener {
             bSSafetyCenterDialog.dismiss()

         }

         bSSafetyCenterDialog.show()
     }*/
}