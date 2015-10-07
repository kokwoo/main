# About
Tempo is a simple tool that allows users to plan and manage their daily schedules using single line commands. With Tempo, you can now add, edit, search, delete, and view events and tasks - all with a single line command!

# Table of Contents
- [Feature Details](#feature-details)
  - [Basic Operations](#basic-operations)
  - [Input Formats](#input-formats)
  - [Manage Your Events](#manage-your-events)
  - [Manage Your Tasks](#manage-your-tasks)
  - [Display Calendar](#display-calendar)
  - [Search](#search)
- [Cheatsheet](#cheatsheet)

# Feature Details
## Basic Operations
To quickly list available commands, use the command `help`. 
To undo your previous operation, use the command `undo`. The most recently made change will then be reverted.

## Input Formats
The input format for dates across all commands should follow this syntax: `DD/MM/YYYY`. If the date input does not exist, you will be alerted and the previous operation with the invalid date input will not be carried out.

The input format for time across all commands should follow the 24-hour format. For example, 2:01PM should be input as `1401` and 4:30AM should be input as `0430`.

## Manage Your Events
To add an event, use the following command. The key `add` can be replaced by `create` or `new`.

    add event <name> from <start date> at <start time> to <end date> at <end time>

If the event is to be repeated, simply append `repeat:<frequency>` to the above command, where the `<frequency>` can be `daily`, `weekly`, `monthly` or `yearly`.

    add event <name> from <start date> at <start time> to <end date> at <end time> repeat:<frequency>

To edit an event, use the following command. It will allow you to modify multiple fields at the same time. The key `edit` can be replaced by `update` or `change`. The `id` of the event can be obtained by viewing all events or searching for the event. Do note that you may append `repeat:<frequency>` if you want to repeat an existing event which is not a recurring event. You may exclude `repeat:<frequency>` if the event you are editing is not a recurring event. 

    edit event <id> <name> from <start date> at <start time> to <end date> at <end time> repeat:<frequency>

If only one of the fields (`<name>`, `<start date>`, `<start time>`, `<end date>`, `<end time>` or `<repeat>`) has to be changed, you may use the following syntax.

    edit event <id> <field name>:<new value>

To repeat an event, use the following command. The possible choices for the frequency of the events are `daily`, `weekly`, `monthly` or `yearly`. The `id` of the event can be obtained by viewing all events or searching for the event.

    repeat event <id> <frequency> until <end date>

To delete an event, use the following command. The key `delete` can be replaced by `cancel` or `remove`. If you delete a recurring event, all instances of the event will be deleted.

    delete event <id>

## Manage Your Tasks
To add a task, use one of the following commands. The key `add` can be replaced by `create` or `new`. Use the second command if a deadline is to be specified.

 - `add task <name>`
 - `add task <name> due <date>`

To edit a task, use the following command. This will allow you to modify multiple fields at the same time. The key `edit` can be replaced by `update` or `change`. The `id` of the task can be obtained by viewing all tasks.

    edit task <id> <name> due <date>

If only one of the fields (`<name>` or `<date>`) has to be changed, you may use the following syntax.

    edit task <id> <field name>:new value>

To delete a task, use the following command. The key `delete` can be replaced by `cancel` or `remove`. If you delete a recurring event, all instances of the event will be deleted.

    delete task <id>

To mark a task as done, use the following command. The key `mark` can be replaced by `flag`.

    mark task done <id>

## Display Calendar
To view the events and tasks due today, use the following command. The key `view` can be replaced by `display`.

    view today

To view the events and tasks due this week, use the following command. The key `view` can be replaced by `display`.

    view week

To view only your events, use the following commands. The key `view` can be replaced by `display`.

 - `view upcoming events`
 - `view events`

To view only your tasks, use the following command. The key `view` can be replaced by `display`.

 - `view undone tasks`
 - `view missed tasks`
 - `view all tasks`


## Search 
To search for an event or task, use the following commands. At least part of the event or task name must be keyed in the `<keywords>` field.

    search <keywords>

# Cheatsheet
