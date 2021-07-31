package com.example.stream_chat_application.ui.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.stream_chat_application.databinding.FragmentChannelBinding
import com.example.stream_chat_application.ui.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.api.models.QuerySort
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.ui.channel.list.header.viewmodel.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory

@AndroidEntryPoint
class ChannelFragment : BindingFragment<FragmentChannelBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChannelBinding::inflate


    private val viewModel : ChannelViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user  = viewModel.getUser()
        if(user == null)
        {
            findNavController().popBackStack()
            return
        }

        val factory = ChannelListViewModelFactory(
            filter = Filters.and(
                Filters.eq("type", "messaging")
            ),
            sort = ChannelListViewModel.DEFAULT_SORT, limit = 40
        )

        val channelListViewModel: ChannelListViewModel by viewModels {factory}

        val channelListHeaderViewModel : ChannelListHeaderViewModel by viewModels()

        channelListViewModel.bindView(binding.channelListView, viewLifecycleOwner)
        channelListHeaderViewModel.bindView(binding.channelListHeaderView, viewLifecycleOwner)

        binding.channelListHeaderView.setOnUserAvatarClickListener{
            viewModel.logout()
            findNavController().popBackStack()
        }
    }
}